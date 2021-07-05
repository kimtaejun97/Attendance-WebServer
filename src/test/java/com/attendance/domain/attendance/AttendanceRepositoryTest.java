package com.attendance.domain.attendance;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class AttendanceRepositoryTest {

    @Autowired
    AttendanceRepository attendanceRepository;

    @Test
    public void save(){
        String attendaceCode = "S";
        String attendanceDate = "2021-07-05";
        String studentId = "164160";
        String lectureCode= "CD123D";

        Attendance attendance = Attendance.builder()
                .attendanceCode(attendaceCode)
                .attendanceDate(attendanceDate)
                .studentId(studentId)
                .lectureCode(lectureCode)
                .build();

        Attendance result =  attendanceRepository.save(attendance);

        assertThat(result.getAttendanceCode()).isEqualTo(attendaceCode);
        assertThat(result.getAttendanceDate()).isEqualTo(attendanceDate);
        assertThat(result.getStudentId()).isEqualTo(studentId);
        assertThat(result.getLectureCode()).isEqualTo(lectureCode);
    }

}