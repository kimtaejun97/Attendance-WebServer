package com.attendance.modules.beacon.form;


import com.attendance.modules.beacon.Beacon;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Data
public class BeaconForm {

    @NotBlank
    private String beaconCode;

    @NotBlank
    private String location;

    @NotBlank
    private String creator;

    private LocalDateTime creationDate;

}
