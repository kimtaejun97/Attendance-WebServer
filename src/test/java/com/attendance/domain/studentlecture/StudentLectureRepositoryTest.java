package com.attendance.domain.studentlecture;

import com.attendance.domain.student.Student;
import com.attendance.domain.student.StudentRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class StudentLectureRepositoryTest {

    @Autowired
    StudentLectureRepository studentLectureRepository;

    @Test
    public void save() {
        String studentId = "164160";
        String lectureCode = "ASDV123";

        StudentLecture studentLecture = StudentLecture.builder()
                .studentId(studentId)
                .lectureCode(lectureCode)
                .build();

        StudentLecture result = studentLectureRepository.save(studentLecture);

        assertThat(result.getStudentId()).isEqualTo(studentId);
        assertThat(result.getLectureCode()).isEqualTo(lectureCode);

    }
}