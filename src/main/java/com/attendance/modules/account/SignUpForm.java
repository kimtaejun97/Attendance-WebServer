package com.attendance.modules.account;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.springframework.security.access.method.P;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
public class SignUpForm {

    @NotBlank
    @Email
    private String email;

    @NotBlank
    @Length(min=3,max=20)
    @Pattern(regexp = "([가-힣a-z0-9_-]{3,20}$)")
    private String nickname;

    @NotBlank
    @Length(min = 8, max = 50)
    private String password;

    private String adminCode;


}
