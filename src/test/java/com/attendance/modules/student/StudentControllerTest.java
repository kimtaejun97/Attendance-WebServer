package com.attendance.modules.student;

import com.attendance.modules.account.Account;
import com.attendance.modules.account.AccountRepository;
import com.attendance.modules.lecture.LectureRepository;
import com.attendance.modules.lecture.form.LectureForm;
import com.attendance.modules.studentlecture.StudentLecture;
import com.attendance.modules.studentlecture.StudentLectureRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
class StudentControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    LectureRepository lectureRepository;

    @Autowired
    StudentLectureRepository studentLectureRepository;

    @Autowired
    AccountRepository accountRepository;

    @BeforeEach
    void initData(){
        LectureForm lectureForm = new LectureForm();
        lectureForm.setLectureCode("ABC123");
        lectureForm.setLectureName("프로그래밍");
        lectureForm.setLectureRoom("211");

        lectureRepository.save(lectureForm.toEntity());

        accountRepository.save(Account.builder()
                .nickname("bigave")
                .email("test@email.com")
                .password("123123123")
                .build());
    }
    @AfterEach
    void cleanup(){
        lectureRepository.deleteAll();
        studentLectureRepository.deleteAll();
        accountRepository.deleteAll();
    }

    @WithMockUser
    @DisplayName("학생 추가 화면")
    @Test
    void addStudentView() throws Exception {

        mockMvc.perform(get("/add-student/ABC123"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/add-student"))
                .andExpect(model().attributeExists("studentForm"));
    }


    @WithMockUser
    @DisplayName("학생 추가 -정상 입력")
    @Test
    void addStudent_with_correct_input() throws Exception {


        mockMvc.perform(post("/add-student/ABC123")
        .param("username","bigave")
        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/lecture/ABC123"))
                .andExpect(model().attributeDoesNotExist("errors"));

        boolean isRegistered = studentLectureRepository.existsByLectureCodeAndStudentName("ABC123", "bigave");

        assertTrue(isRegistered);

    }
    @WithMockUser
    @DisplayName("학생 추가 -존재하지 않는 사용자")
    @Test
    void addStudent_with_nonexist_user() throws Exception {

        mockMvc.perform(post("/add-student/ABC123")
                .param("username","nonono")
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/add-student"))
                .andExpect(model().hasErrors());

        boolean isRegistered = studentLectureRepository.existsByLectureCodeAndStudentName("ABC123", "bigave");
        assertFalse(isRegistered);
    }

    @WithMockUser
    @DisplayName("학생 추가 -이미 등록된 사용자")
    @Test
    void addStudent_duplicated_user() throws Exception {

        studentLectureRepository.save(StudentLecture.builder()
                .studentName("bigave")
                .lectureCode("ABC123")
                .build());

        mockMvc.perform(post("/add-student/ABC123")
                .param("username","bigave")
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/add-student"))
                .andExpect(model().hasErrors());


    }

}