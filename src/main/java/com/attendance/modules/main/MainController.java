package com.attendance.modules.main;

import com.attendance.modules.attendance.AttendanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@RequiredArgsConstructor
@Controller
public class MainController {

    private final AttendanceService attendanceService;

    @GetMapping("/")
    public String index(Model model){
        return "index";
    }

    @GetMapping("/admin")
    public String admin(Model model){
        model.addAttribute("lectures",attendanceService.showLectureList());
        return "admin/admin";
    }

    @GetMapping("/attendance")
    public String attendance(){
        return "attendance";
    }



    @GetMapping("student_list")
    public String student_list(){
        return "admin/student_list";
    }

}

