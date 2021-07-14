package com.attendance.modules.lecture;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Getter @Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Lecture {

    @Id
    private String lectureCode;

    @Column(nullable = false)
    private String lectureName;

    @Column(nullable = false)
    private String lectureRoom;



}
