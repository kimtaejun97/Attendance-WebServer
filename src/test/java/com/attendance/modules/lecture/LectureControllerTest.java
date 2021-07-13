package com.attendance.modules.lecture;

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

    @WithMockUser
    @DisplayName("mylectue 페이지")
    @Test
    void myLecture() throws Exception {

        mockMvc.perform(get("/my-lecture"))
                .andExpect(status().isOk())
                .andExpect(view().name("student/my-lecture"));
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




}