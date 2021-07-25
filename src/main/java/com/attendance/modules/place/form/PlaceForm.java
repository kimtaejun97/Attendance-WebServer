package com.attendance.modules.place.form;

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
    private String creator;

    private LocalDateTime creationDate;

    private String isPublic;


}
