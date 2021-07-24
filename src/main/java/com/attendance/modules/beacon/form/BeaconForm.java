package com.attendance.modules.beacon.form;


import com.attendance.modules.beacon.Beacon;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class BeaconForm {

    @NotBlank
    private String beaconCode;

    @NotBlank
    private String location;

    @NotBlank
    private String creator;

}
