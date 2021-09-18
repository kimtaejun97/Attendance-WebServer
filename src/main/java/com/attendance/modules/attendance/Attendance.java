package com.attendance.modules.attendance;

import com.attendance.modules.account.Account;
import com.attendance.modules.attendance.form.AttendanceRequestDto;
import com.attendance.modules.place.Place;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
@Entity
public class Attendance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Account account;

    @ManyToOne
    private Place place;

    @Column(nullable = false)
    private LocalDateTime attendanceDate;

    @Column(nullable = false)
    private String attendanceCode;


    public Attendance(AttendanceRequestDto requestDto) {
        this.account = requestDto.getAccount();
        this.place = requestDto.getPlace();
        this.attendanceCode = requestDto.getAttendanceCode();
        this.attendanceDate = requestDto.getAttendanceDate();
    }
}
