package com.attendance.modules.place.form;

import com.attendance.modules.account.Account;
import com.attendance.modules.beacon.Beacon;
import com.attendance.modules.place.Place;
import lombok.*;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Data
public class PlaceForm {

    @NotBlank
    private String location;

    @NotBlank
    private String alias;

    @NotBlank
    private String creatorName;

    private Account creator;

    private LocalDateTime creationDate;

    private boolean isPublic;

    private Beacon beacon;




}
