package com.attendance.web.dto;

import com.attendance.domain.lecture.Lecture;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

public class LectureListResponseDtoTest {


    @Test
    public void create(){
        String lectureCode = "AVB214";
        String lectureName = "Spring";
        String lectureRoom = "211호";

        Lecture lecture = Lecture.builder()
                .lectureCode(lectureCode)
                .lectureName(lectureName)
                .lectureRoom(lectureRoom)
                .build();

        LectureListResponseDto lectureListResponseDto = new LectureListResponseDto(lecture);

        assertThat(lectureListResponseDto.getLectureCode()).isEqualTo(lectureCode);
        assertThat(lectureListResponseDto.getLectureRoom()).isEqualTo(lectureRoom);
        assertThat(lectureListResponseDto.getLectureName()).isEqualTo(lectureName);
    }
}