package com.attendance.modules.attendance;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
public class Attendance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String studentId;

    @Column(nullable = false)
    private String lectureCode;

    @Column(nullable = false)
    private String attendanceDate;

    @Column(nullable = false)
    private String attendanceCode;


    @Builder
    public Attendance(String studentId, String lectureCode, String attendanceDate, String attendanceCode){
        this.studentId = studentId;
        this.lectureCode =lectureCode;
        this.attendanceDate = attendanceDate;
        this.attendanceCode = attendanceCode;
    }

}
