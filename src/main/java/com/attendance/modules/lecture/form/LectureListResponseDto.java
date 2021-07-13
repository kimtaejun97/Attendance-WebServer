package com.attendance.modules.lecture.form;

import com.attendance.modules.lecture.Lecture;
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
