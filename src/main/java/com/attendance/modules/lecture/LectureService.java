package com.attendance.modules.lecture;

import com.attendance.modules.lecture.form.LectureListResponseDto;
import com.attendance.modules.lecture.form.LectureForm;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;
@Transactional
@RequiredArgsConstructor
@Service
public class LectureService {

    private final LectureRepository lectureRepository;

    public String addLecture(LectureForm lectureSaveRequestDto) {

        return lectureRepository.save(lectureSaveRequestDto.toEntity()).getLectureName();
    }

    public List<LectureListResponseDto> showLectureList() {
        return lectureRepository.findAll().stream()
                .map(LectureListResponseDto::new)
                .collect(Collectors.toList());
    }
}
