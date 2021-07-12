package com.attendance.modules.student;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Getter
@NoArgsConstructor
@Entity
public class Student {

    @Column(name = "studentId")
    @Id
    private String studentId;

    @Column(nullable = false)
    private String studentName;

    @Builder
    public Student(String studentId, String studentName){
        this.studentId = studentId;
        this.studentName = studentName;
    }

}
