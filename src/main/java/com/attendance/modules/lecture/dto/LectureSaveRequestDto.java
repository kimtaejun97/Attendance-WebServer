package com.attendance.modules.lecture.dto;

import com.attendance.modules.lecture.Lecture;
import lombok.*;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
@NoArgsConstructor
public class LectureSaveRequestDto {

    @NotEmpty
    private String lectureCode;
    @NotEmpty
    private String lectureName;
    @NotEmpty
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
