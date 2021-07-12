package com.attendance.modules.attendance;

import com.attendance.modules.lecture.LectureRepository;
import com.attendance.modules.lecture.dto.LectureListResponseDto;
import com.attendance.modules.lecture.dto.LectureSaveRequestDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AttendanceService {

    @Autowired
    LectureRepository lectureRepository;

    @Transactional
    public String addLecture(LectureSaveRequestDto lectureSaveRequestDto) {
        return lectureRepository.save(lectureSaveRequestDto.toEntity()).getLectureName();
    }

    public List<LectureListResponseDto> showLectureList() {
        return lectureRepository.findAll().stream()
                .map(LectureListResponseDto::new)
                .collect(Collectors.toList());
    }
}
