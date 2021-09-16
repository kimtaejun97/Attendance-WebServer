package com.attendance.modules.place.form;

import com.attendance.modules.account.Account;
import com.attendance.modules.beacon.Beacon;
import com.attendance.modules.place.Place;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Data
public class PlaceListResponseDto {
    private String alias;

    private Account creator;

    private LocalDateTime creationDate;

    private boolean isPublic;

    private Beacon beacon;
}
