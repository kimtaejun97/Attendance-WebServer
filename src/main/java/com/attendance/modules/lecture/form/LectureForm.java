package com.attendance.modules.lecture.form;

import com.attendance.modules.lecture.Lecture;
import lombok.*;

import javax.validation.constraints.NotBlank;

@Data
public class LectureForm {

    @NotBlank
    private String lectureCode;

    @NotBlank
    private String lectureName;

    @NotBlank
    private String lectureRoom;

    public Lecture toEntity(){
        return Lecture.builder()
                .lectureCode(lectureCode)
                .lectureRoom(lectureRoom)
                .lectureName(lectureName)
                .build();
    }

}
