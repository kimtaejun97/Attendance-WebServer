package com.attendance.modules.attendance;

import com.attendance.modules.account.Account;
import com.attendance.modules.account.CurrentUser;
import com.attendance.modules.lecture.Lecture;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.Map;


@RequiredArgsConstructor
@Controller
public class AttendanceController {
    private final HttpSession httpSession;


//    @GetMapping("/attendance")
//    public String attendance(@CurrentUser Account account,@PathVariable String beaconCode Model model){
//        //TODO Beacon Code 받아 넘기기.
////        Lecture lecture =  attendanceService.findLectureFromBeaconCode("bbbbbbbbb");
//
//        model.addAttribute(account);
//
//        return "student/attendance";
//    }







}
