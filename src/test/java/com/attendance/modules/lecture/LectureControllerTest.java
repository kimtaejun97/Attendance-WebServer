package com.attendance.modules.lecture;

import com.attendance.modules.account.Account;
import com.attendance.modules.account.AccountService;
import com.attendance.modules.account.SignUpForm;
import com.attendance.modules.lecture.form.LectureForm;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class LectureControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    LectureService lectureService;

    @Autowired
    LectureRepository lectureRepository;

    @Autowired
    AccountService accountService;

    @AfterEach
    void cleanup(){
        lectureRepository.deleteAll();
    }


    @WithMockUser
    @DisplayName("admin 페이지")
    @Test
    void adminPage() throws Exception {

        mockMvc.perform(get("/admin-page"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/admin-page"))
                .andExpect(model().attributeExists("lectures"));
    }

    @WithMockUser
    @DisplayName("강의 추가 View")
    @Test
    void addLectureView() throws Exception {

        mockMvc.perform(get("/create-lecture"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/create-lecture"))
                .andExpect(model().attributeExists("lectureForm"));
    }

    @WithMockUser
    @DisplayName("강의 추가 - 입력값 정상")
    @Test
    void addLecture_with_correct_input() throws Exception {

        mockMvc.perform(post("/create-lecture")
        .param("lectureCode", "ASDF134")
        .param("lectureName", "프로그래밍")
        .param("lectureRoom", "211호")
        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/admin-page"));
    }

    @WithMockUser
    @DisplayName("강의 추가 - 입력값 오류")
    @Test
    void addLecture_with_wrong_input() throws Exception {

        mockMvc.perform(post("/create-lecture")
                .param("lectureName", "프로그래밍")
                .param("lectureRoom", "211호")
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/create-lecture"));
    }

    @WithMockUser
    @DisplayName("강의 추가 - 이미 존재하는 강의")
    @Test
    void addLecture_with_exists_input() throws Exception {

        mockMvc.perform(post("/create-lecture")
                .param("lectureCode", "ASDF134")
                .param("lectureName", "프로그래밍")
                .param("lectureRoom", "211호")
                .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/admin-page"));

        mockMvc.perform(post("/create-lecture")
                .param("lectureCode", "ASDF134")
                .param("lectureName", "프로그래밍")
                .param("lectureRoom", "211호")
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/create-lecture"));
    }

    @WithMockUser
    @DisplayName("강의 정보 View")
    @Test
    void lectureInfoView() throws Exception {
        LectureForm lectureForm = new LectureForm();
        lectureForm.setLectureCode("ABC123");
        lectureForm.setLectureName("프로그래밍");
        lectureForm.setLectureRoom("211");

       lectureService.addLecture(lectureForm);

        mockMvc.perform(get("/lecture/ABC123"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/lecture"))
                .andExpect(model().attributeExists("lecture"))
                .andExpect(model().attributeExists("students"));
    }


    @DisplayName("나의 강의 페이지")
    @Test
    void myLecture() throws Exception {
        SignUpForm signUpForm = new SignUpForm();
        signUpForm.setNickname("bigave");
        signUpForm.setAdminCode("");
        signUpForm.setPassword("123123123");
        signUpForm.setEmail("test@email.com");

        Account newAccount = accountService.createNewAccount(signUpForm);
        accountService.login(newAccount);

        mockMvc.perform(get("/my-lecture"))
                .andExpect(status().isOk())
                .andExpect(view().name("student/my-lecture"))
                .andExpect(model().attributeExists("lectures"));
    }




}