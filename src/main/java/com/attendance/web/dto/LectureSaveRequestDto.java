package com.attendance.web.dto;

import com.attendance.domain.lecture.Lecture;
import lombok.*;

import javax.persistence.Column;
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
