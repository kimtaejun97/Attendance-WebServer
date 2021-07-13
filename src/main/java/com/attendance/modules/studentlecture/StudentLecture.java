package com.attendance.modules.studentlecture;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
public class StudentLecture {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    @Column(nullable = false)
    private Long studentId;

    @Column(nullable = false)
    private String lectureCode;

    @Builder
    public StudentLecture(Long studentId, String lectureCode){
        this.studentId = studentId;
        this.lectureCode = lectureCode;

    }
}
