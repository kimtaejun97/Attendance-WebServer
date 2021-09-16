package com.attendance.modules.place.form;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
public class AddUserForm {

    @NotBlank
    private String username;

    private String location;


    public AddUserForm(String location) {
        this.location = location;
    }
}
