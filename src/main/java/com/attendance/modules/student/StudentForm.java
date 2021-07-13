package com.attendance.modules.student;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class StudentForm {

    @NotBlank
    private String username;


}
