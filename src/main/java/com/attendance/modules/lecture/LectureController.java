package com.attendance.modules.lecture;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LectureController {
    @GetMapping("/my_lecture")
    public String my_lecture(){
        return "student/my_lecture";
    }
}
