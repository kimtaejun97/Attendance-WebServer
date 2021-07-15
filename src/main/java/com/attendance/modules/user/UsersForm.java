package com.attendance.modules.user;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class UsersForm {

    @NotBlank
    private String username;

    @NotBlank
    private String location;


}
