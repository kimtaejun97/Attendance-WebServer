package com.attendance.modules.lecture.dto;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

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