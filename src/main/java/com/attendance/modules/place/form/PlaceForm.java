package com.attendance.modules.place.form;

import com.attendance.modules.place.Place;
import lombok.*;

import javax.validation.constraints.NotBlank;

@Data
public class PlaceForm {

    @NotBlank
    private String location;

    @NotBlank
    private String alias;

    @NotBlank
    private String creator;

    String isPublic;


}
