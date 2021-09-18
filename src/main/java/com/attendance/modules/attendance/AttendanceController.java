package com.attendance.modules.attendance;

import com.attendance.modules.account.Account;
import com.attendance.modules.account.CurrentUser;
import com.attendance.modules.attendance.form.AttendanceRequestDto;
import com.attendance.modules.place.Place;
import com.attendance.modules.place.PlaceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Controller
public class AttendanceController {

    private final PlaceService placeService;
    private final AttendanceService attendanceService;

    //TODO 출결 현황 : /attendance/{location} creator == currentUser 이면 방 유저들 목록, 클릭시 각자 출결 현황 + 자신의 출결
    // 그냥 사용자일 경우 자신의 출결 현황 반환, Template : user/attendance


    @GetMapping("/attendance/my/{location}")
    public String userPlaceInfo(@PathVariable String location, @CurrentUser Account account, Model model){
        Place place =placeService.getPlace(location);
        List<Attendance> attendances =  attendanceService.getAttendances(place, account);
        model.addAttribute("place", place);
        model.addAttribute("attendances", attendances);

        return "user/my-attendance";
    }

    @GetMapping("/check")
    public String attendanceView(){
        return "attendance/check";
    }

    @PostMapping("/checkIn")
    public String checkIn(@CurrentUser Account account, String location){
        attendanceService.checkIn(location, account);
        return "redirect:/attendance/my/"+ encode(location);
    }

    private String encode(String location) {
        return URLEncoder.encode(location, StandardCharsets.UTF_8);
    }

    @PostMapping("/checkOut")
    public String checkOut(@CurrentUser Account account, String location){
        attendanceService.checkOut(location, account);
        return "redirect:/attendance/my/"+encode(location);
    }

    @GetMapping("/check/{beaconCode}")
    public String getPlaceByBeaconId(@CurrentUser Account account, @PathVariable String beaconCode, Model model){
        Place place = placeService.findByBeaconCode(beaconCode);
        model.addAttribute(new AttendanceRequestDto());
        model.addAttribute("place", place);
        model.addAttribute("canCheckIn",attendanceService.canCheckIn(account, place));

        return "attendance/check";
    }
}
