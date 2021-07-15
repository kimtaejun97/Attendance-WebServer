package com.attendance.modules.attendance;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@Entity
public class Attendance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String location;

    @Column(nullable = false)
    private LocalDateTime attendanceDate;

    @Column(nullable = false)
    private String attendanceCode;


    @Builder
    public Attendance(String username, String location, LocalDateTime attendanceDate, String attendanceCode){
        this.username = username;
        this.location = location;
        this.attendanceDate = attendanceDate;
        this.attendanceCode = attendanceCode;
    }

}
