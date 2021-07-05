package com.attendance.domain.student;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;


@DataJpaTest
public class StudentRepositoryTest {

    @Autowired
    StudentRepository studentRepository;

    @Test
    public void save(){
        String studentId = "164160";
        String studentName = "bigave";

        Student student = Student.builder()
                .studentId(studentId)
                .studentName(studentName)
                .build();

        Student result = studentRepository.save(student);

        assertThat(result.getStudentId()).isEqualTo(studentId);
        assertThat(result.getStudentName()).isEqualTo(studentName);


    }



}