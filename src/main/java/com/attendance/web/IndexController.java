package com.attendance.web;

import com.attendance.service.AttendanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@Controller
public class IndexController {

    private final AttendanceService attendanceService;

    @GetMapping("/")
    public String showLectureList(Model model){
        model.addAttribute("lectures",attendanceService.showLectureList());

        return "index";
    }

}

