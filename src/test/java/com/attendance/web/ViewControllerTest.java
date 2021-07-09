package com.attendance.web;

import com.attendance.domain.lecture.Lecture;
import com.attendance.service.AttendanceService;
import com.attendance.web.dto.LectureListResponseDto;
import org.hamcrest.collection.IsCollectionWithSize;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.contains;

import java.util.ArrayList;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest
public class ViewControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    AttendanceService mockAttendanceService;

    @Test
    public void index() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("index"));

    }

    @Test
    public void admin() throws Exception {
        String lectureCode = "AVB214";
        String lectureName = "Spring";
        String lectureRoom = "211호";

        Lecture lecture = Lecture.builder()
                .lectureCode(lectureCode)
                .lectureName(lectureName)
                .lectureRoom(lectureRoom)
                .build();
        LectureListResponseDto lectureListResponseDto = new LectureListResponseDto(lecture);

        var lectures = new ArrayList<LectureListResponseDto>();
        lectures.add(lectureListResponseDto);

        when(mockAttendanceService.showLectureList()).thenReturn(lectures);


        mockMvc.perform(get("/admin"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin"))
                .andExpect(model().attributeExists("lectures"))
                .andExpect(model().attribute("lectures", IsCollectionWithSize.hasSize(1)))
                .andExpect(model().attribute("lectures",contains(lectures.get(0))));
    }

    @Test
    public void attendance() throws Exception {

        mockMvc.perform(get("/attendance"))
                .andExpect(status().isOk())
                .andExpect(view().name("attendance"));
    }

    @Test
    public void my_lecture() throws Exception {

        mockMvc.perform(get("/my_lecture"))
                .andExpect(status().isOk())
                .andExpect(view().name("my_lecture"));
    }

    @Test
    public void signup() throws Exception {

        mockMvc.perform(get("/signup"))
                .andExpect(status().isOk())
                .andExpect(view().name("signup"));

    }

    @Test
    public void student_list() throws Exception {

        mockMvc.perform(get("/student_list"))
                .andExpect(status().isOk())
                .andExpect(view().name("student_list"));

    }
}