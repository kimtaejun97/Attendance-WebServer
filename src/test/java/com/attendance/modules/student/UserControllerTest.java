package com.attendance.modules.student;

import com.attendance.modules.account.Account;
import com.attendance.modules.account.AccountRepository;
import com.attendance.modules.lecture.PlaceRepository;
import com.attendance.modules.lecture.form.PlaceForm;
import com.attendance.modules.studentlecture.UserLocation;
import com.attendance.modules.studentlecture.UserLocationRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    PlaceRepository placeRepository;

    @Autowired
    UserLocationRepository userLocationRepository;

    @Autowired
    AccountRepository accountRepository;

    @BeforeEach
    void initData(){
        PlaceForm placeForm = new PlaceForm();
        placeForm.setLocation("광주");
        placeForm.setAlias("내 지역");
        placeForm.setConstructor("bigave");
        placeForm.setIsPublic("true");

        placeRepository.save(placeForm.toEntity());

        accountRepository.save(Account.builder()
                .nickname("bigave")
                .email("test@email.com")
                .password("123123123")
                .build());
    }
    @AfterEach
    void cleanup(){
        placeRepository.deleteAll();
        userLocationRepository.deleteAll();
        accountRepository.deleteAll();
    }

    @WithMockUser
    @DisplayName("학생 추가 화면")
    @Test
    void addStudentView() throws Exception {

        mockMvc.perform(get("/add-user/광주"))
                .andExpect(status().isOk())
                .andExpect(view().name("user/add-user"))
                .andExpect(model().attributeExists("userForm"));
    }


    @WithMockUser
    @DisplayName("학생 추가 -정상 입력")
    @Test
    void addStudent_with_correct_input() throws Exception {


        mockMvc.perform(post("/add-user/광주")
        .param("username","bigave")
        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/user/place/광주"))
                .andExpect(model().attributeDoesNotExist("errors"));

        boolean isRegistered = userLocationRepository.existsByLocationAndUsername("광주", "bigave");

        assertTrue(isRegistered);

    }
    @WithMockUser
    @DisplayName("학생 추가 -존재하지 않는 사용자")
    @Test
    void addStudent_with_nonexist_user() throws Exception {

        mockMvc.perform(post("/add-user/광주")
                .param("username","nonono")
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("user/add-user"))
                .andExpect(model().hasErrors());

        boolean isRegistered = userLocationRepository.existsByLocationAndUsername("광주", "bigave");
        assertFalse(isRegistered);
    }

    @WithMockUser
    @DisplayName("학생 추가 -이미 등록된 사용자")
    @Test
    void addStudent_duplicated_user() throws Exception {

        userLocationRepository.save(UserLocation.builder()
                .username("bigave")
                .location("광주")
                .build());

        mockMvc.perform(post("/add-user/광주")
                .param("username","bigave")
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("user/add-user"))
                .andExpect(model().hasErrors());

    }

}