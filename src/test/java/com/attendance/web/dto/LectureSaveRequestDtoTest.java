package com.attendance.web.dto;

import com.attendance.domain.lecture.Lecture;
import lombok.NonNull;
import org.junit.Test;

import javax.validation.Valid;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

public class LectureSaveRequestDtoTest {

    @Test
    public void create(){
        String lectureCode = "AVB214";
        String lectureName = "Spring";
        String lectureRoom = "211호";

        LectureSaveRequestDto lectureSaveRequestDto = LectureSaveRequestDto.builder()
                .lectureCode(lectureCode)
                .lectureName(lectureName)
                .lectureRoom(lectureRoom)
                .build();

        assertThat(lectureSaveRequestDto.getLectureCode()).isEqualTo(lectureCode);
        assertThat(lectureSaveRequestDto.getLectureRoom()).isEqualTo(lectureRoom);
        assertThat(lectureSaveRequestDto.getLectureName()).isEqualTo(lectureName);


    }

}