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
    private String constructor;

    @NotBlank
    private String isPublic;


    public Place toEntity(){
        return Place.builder()
                .location(location)
                .alias(alias)
                .constructor(constructor)
                .isPublic(isPublic)
                .build();
    }

}