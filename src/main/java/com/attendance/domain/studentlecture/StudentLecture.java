package com.attendance.domain.studentlecture;

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
    private String studentId;

    @Column(nullable = false)
    private String lectureCode;

    @Builder
    public StudentLecture(String studentId, String lectureCode){
        this.studentId = studentId;
        this.lectureCode = lectureCode;
    }
}
