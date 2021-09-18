package com.attendance.modules.attendance.form;

import com.attendance.modules.account.Account;
import com.attendance.modules.place.Place;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AttendanceRequestDto {

    private Account account;

    private Place place;

    private String location;

    private LocalDateTime attendanceDate;

    private String attendanceCode;
}
