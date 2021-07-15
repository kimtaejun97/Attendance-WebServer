package com.attendance.modules.student;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class UserForm {

    @NotBlank
    private String username;

    @NotBlank
    private String location;


}
