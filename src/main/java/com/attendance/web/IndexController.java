package com.attendance.web;

import com.attendance.service.AttendanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@RequiredArgsConstructor
@Controller
public class IndexController {

    private final AttendanceService attendanceService;

    @GetMapping("/")
    public String showLectureList(Model model){
        model.addAttribute("lectures",attendanceService.showLectureList());

        return "admin";
    }

}

