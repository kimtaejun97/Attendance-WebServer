package com.attendance.modules.attendance;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;

import javax.servlet.http.HttpSession;


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
