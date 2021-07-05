package com.attendance.web;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AttendanceController {


    @GetMapping("/")
    public String index(){
        return "춟석부 관리 시스템";
    }

}
