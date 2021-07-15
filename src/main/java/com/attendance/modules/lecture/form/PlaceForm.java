package com.attendance.modules.lecture.form;

import com.attendance.modules.lecture.Place;
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
    private boolean isPublic;


    public Place toEntity(){
        return Place.builder()
                .location(location)
                .alias(alias)
                .constructor(constructor)
                .isPublic(isPublic)
                .build();
    }

}
