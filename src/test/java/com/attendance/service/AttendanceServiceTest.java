package com.attendance.service;

import com.attendance.domain.lecture.Lecture;
import com.attendance.domain.lecture.LectureRepository;
import com.attendance.web.dto.LectureSaveRequestDto;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class AttendanceServiceTest {

    @Autowired
    LectureRepository lectureRepository;

    @Autowired
    AttendanceService attendanceService;

    @Test
    public void addLecture(){
        String name = "프로그래밍";
        String code = "asd123";
        String room = "211호";

        LectureSaveRequestDto lectureSaveRequestDto = LectureSaveRequestDto.builder()
                .lectureCode(code)
                .lectureName(name)
                .lectureRoom(room)
                .build();

        String responseName = attendanceService.addLecture(lectureSaveRequestDto);

        assertThat(responseName).isEqualTo(name);

        Lecture lecture = lectureRepository.findAll().get(0);

        assertThat(lecture.getLectureRoom()).isEqualTo(room);
        assertThat(lecture.getLectureName()).isEqualTo(name);
        assertThat(lecture.getLectureCode()).isEqualTo(code);

    }


}