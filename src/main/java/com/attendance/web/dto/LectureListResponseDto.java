package com.attendance.web.dto;

import com.attendance.domain.lecture.Lecture;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class LectureListResponseDto {
    private String lectureCode;
    private String lectureName;
    private String lectureRoom;


    public LectureListResponseDto(Lecture lecture){
        this.lectureCode = lecture.getLectureCode();
        this.lectureName = lecture.getLectureName();
        this.lectureRoom = lecture.getLectureRoom();
    }


}
