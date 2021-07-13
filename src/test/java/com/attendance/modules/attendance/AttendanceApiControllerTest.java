package com.attendance.modules.attendance;

import com.attendance.modules.lecture.Lecture;
import com.attendance.modules.lecture.LectureRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
public class AttendanceApiControllerTest {

    @Autowired
    LectureRepository lectureRepository;

    MockMvc mockMvc;

    @Before
    public void cleanup(){
        lectureRepository.deleteAll();
    }

    @Test
    public void addLecture() throws Exception {

        String jsonData = "{\"lectureName\":\"programming\",\"lectureCode\":\"abc123\",\"lectureRoom\":\"211호\"}";
        String wrongJsonData = "{\"lectureName\":\"programming\",\"lectureCode\":\"abc123\"}";

        mockMvc.perform(post("/api/lecture")
            .content(jsonData)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("programming"))
                .andDo(print());

        Lecture lecture = lectureRepository.findAll().get(0);

        assertThat(lecture.getLectureName()).isEqualTo("programming");
        assertThat(lecture.getLectureCode()).isEqualTo("abc123");
        assertThat(lecture.getLectureRoom()).isEqualTo("211호");

        mockMvc.perform(post("/api/lecture")
                .content(wrongJsonData)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andDo(print());



    }

}