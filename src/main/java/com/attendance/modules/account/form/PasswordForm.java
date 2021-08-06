package com.attendance.modules.account.form;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

@Data
public class PasswordForm {

    @NotBlank
    private String username;

    @NotBlank
    private String currentPassword;

    @NotBlank
    @Length(min=8, max=50)
    private String newPassword;

    @NotBlank
    @Length(min=8, max=50)
    private String confirmPassword;
}
