package com.attendance.modules.accountplace.form;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class UserForm {

    @NotBlank
    private String username;


}
