package com.attendance.modules.student;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;


@DataJpaTest
class StudentRepositoryTest {

    @Autowired
    StudentRepository studentRepository;

    @Test
    void save(){
        String studentId = "164160";
        String studentName = "bigave";

        Student student = Student.builder()
                .studentName(studentName)
                .build();

        Student result = studentRepository.save(student);

        assertThat(result.getStudentId()).isEqualTo(studentId);
        assertThat(result.getStudentName()).isEqualTo(studentName);


    }



}