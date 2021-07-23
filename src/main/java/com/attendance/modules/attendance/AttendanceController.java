package com.attendance.modules.attendance;

import com.attendance.modules.account.Account;
import com.attendance.modules.account.CurrentUser;
import com.attendance.modules.place.PlaceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpSession;


@RequiredArgsConstructor
@Controller
public class AttendanceController {
    private final HttpSession httpSession;

    private final PlaceService placeService;

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

    @GetMapping("/attendance/my/{location}")
    public String userPlaceInfo(@PathVariable String location, @CurrentUser Account account, Model model){
        boolean isConstructor = placeService.isCreator(location, account.getUsername());

        model.addAttribute("place", placeService.getPlace(location));
        if(isConstructor){
            model.addAttribute("isConstructor",isConstructor);
        }
        return "user/my-attendance";
    }


    //TODO 출결 현황에서 생성자일 경우 유저 제거 기능.







}
