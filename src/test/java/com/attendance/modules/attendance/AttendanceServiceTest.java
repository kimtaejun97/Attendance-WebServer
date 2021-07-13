package com.attendance.modules.attendance;

import com.attendance.modules.attendance.AttendanceService;
import com.attendance.modules.lecture.Lecture;
import com.attendance.modules.lecture.LectureRepository;
import com.attendance.modules.lecture.dto.LectureSaveRequestDto;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AttendanceServiceTest {

    @Autowired
    LectureRepository lectureRepository;

    @Autowired
    AttendanceService attendanceService;

    @Before
    public void cleanup(){
        lectureRepository.deleteAll();
    }

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