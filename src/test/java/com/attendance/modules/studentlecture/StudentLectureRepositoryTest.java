package com.attendance.modules.studentlecture;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class StudentLectureRepositoryTest {

    @Autowired
    StudentLectureRepository studentLectureRepository;

    @Test
    public void save() {
        String lectureCode = "ASDV123";
        String studentName = "bigave";

        StudentLecture studentLecture = StudentLecture.builder()
                .lectureCode(lectureCode)
                .studentName(studentName)
                .build();

        StudentLecture result = studentLectureRepository.save(studentLecture);

        assertThat(result.getLectureCode()).isEqualTo(lectureCode);
        assertThat(result.getStudentName()).isEqualTo(studentName);

    }
}