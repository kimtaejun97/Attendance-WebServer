package com.attendance.modules.attendance;

import com.attendance.modules.lecture.dto.LectureSaveRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
public class AttendanceApiController {
    private final AttendanceService attendanceService;

    @PostMapping("/api/lecture")
    public String addLecture(@RequestBody @Valid LectureSaveRequestDto lectureSaveRequestDto){
        return attendanceService.addLecture(lectureSaveRequestDto);
    }





}
