package com.attendance.domain.lecture;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Getter
@NoArgsConstructor
@Entity
public class Lecture {

    @Id
    private String lectureCode;

    @Column(nullable = false)
    private String lectureName;

    @Column(nullable = false)
    private String lectureRoom;

    @Builder
    public Lecture(String lectureCode, String lectureName, String lectureRoom){
        this.lectureCode = lectureCode;
        this.lectureName = lectureName;
        this.lectureRoom = lectureRoom;
    }

}
