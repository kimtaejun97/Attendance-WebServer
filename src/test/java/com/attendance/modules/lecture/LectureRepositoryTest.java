package com.attendance.modules.lecture;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class LectureRepositoryTest {

    @Autowired
    LectureRepository lectureRepository;

    @Test
    public void save(){

        String lectureCode = "AVB214";
        String lectureName = "Spring";
        String lectureRoom = "211호";

        Lecture lecture = Lecture.builder()
                .lectureCode(lectureCode)
                .lectureName(lectureName)
                .lectureRoom(lectureRoom)
                .build();

        Lecture result = lectureRepository.save(lecture);

        assertThat(result.getLectureCode()).isEqualTo(lectureCode);
        assertThat(result.getLectureName()).isEqualTo(lectureName);
        assertThat(result.getLectureRoom()).isEqualTo(lectureRoom);

    }
}