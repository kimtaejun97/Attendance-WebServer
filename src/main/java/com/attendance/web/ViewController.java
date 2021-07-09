package com.attendance.web;

import com.attendance.service.AttendanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@RequiredArgsConstructor
@Controller
public class ViewController {

    private final AttendanceService attendanceService;

    @GetMapping("/")
    public String index(Model model){
        return "index";
    }
    @GetMapping("/admin")
    public String admin(Model model){
        model.addAttribute("lectures",attendanceService.showLectureList());
        return "admin";
    }

    @GetMapping("/attendance")
    public String attendance(){
        return "attendance";
    }

    @GetMapping("/my_lecture")
    public String my_lecture(){
        return "my_lecture";
    }

    @GetMapping("/signup")
    public String signup(){
        return "signup";
    }

    @GetMapping("student_list")
    public String student_list(){
        return "student_list";
    }

}

