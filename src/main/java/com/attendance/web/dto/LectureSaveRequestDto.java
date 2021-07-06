package com.attendance.web.dto;

import com.attendance.domain.lecture.Lecture;
import lombok.*;

import javax.persistence.Column;

@Getter
@Setter
@NoArgsConstructor
public class LectureSaveRequestDto {
    private String lectureCode;
    private String lectureName;
    private String lectureRoom;

    @Builder
    public LectureSaveRequestDto(String lectureCode, String lectureName, String lectureRoom){
        this.lectureCode = lectureCode;
        this.lectureName = lectureName;
        this.lectureRoom = lectureRoom;
    }

    public Lecture toEntity(){
        Lecture lecture = Lecture.builder()
                .lectureName(lectureName)
                .lectureCode(lectureCode)
                .lectureRoom(lectureRoom)
                .build();
        return lecture;
    }

}
