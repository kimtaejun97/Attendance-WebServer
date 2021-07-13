package com.attendance.modules.student;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Getter
@NoArgsConstructor
@Entity
public class Student {

    @Id @GeneratedValue
    private Long studentId;

    @Column(nullable = false)
    private String studentName;

    @Builder
    public Student(String studentName){
        this.studentName = studentName;
    }

}
