package com.attendance.modules.attendance;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class AttendanceRepositoryTest {

    @Autowired
    AttendanceRepository attendanceRepository;

    @Test
    void save(){
        String attendaceCode = "S";
        LocalDateTime attendanceDate = LocalDateTime.now();
        String username = "bigave";
        String lectureCode= "CD123D";

        Attendance attendance = Attendance.builder()
                .attendanceCode(attendaceCode)
                .attendanceDate(attendanceDate)
                .username(username)
                .lectureCode(lectureCode)
                .build();

        Attendance result =  attendanceRepository.save(attendance);

        assertThat(result.getAttendanceCode()).isEqualTo(attendaceCode);
        assertThat(result.getAttendanceDate()).isEqualTo(attendanceDate);
        assertThat(result.getUsername()).isEqualTo(username);
        assertThat(result.getLectureCode()).isEqualTo(lectureCode);
    }

}