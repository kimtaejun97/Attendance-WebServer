package com.attendance.modules.attendance;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;

import javax.servlet.http.HttpSession;


@RequiredArgsConstructor
@Controller
public class AttendanceController {
    private final HttpSession httpSession;

    //TODO 출결 현황 : /attendance/{location} creator == currentUser 이면 방 유저들 목록, 클릭시 각자 출결 현황 + 자신의 출결
    // 그냥 사용자일 경우 자신의 출결 현황 반환, Template : user/attendance


    //TODO Check 하기  template : user/check
//    @GetMapping("/check")
//    public String attendance(@CurrentUser Account account,@PathVariable String beaconCode Model model){
//        //TODO Beacon Code 받아 넘기기.
////        Lecture lecture =  attendanceService.findLectureFromBeaconCode("bbbbbbbbb");
//
//        model.addAttribute(account);
//
//        return "student/attendance";
//    }


    //TODO 출결 현황에서 생성자일 경우 유저 제거 기능.







}
