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
public class User {

    @Id @GeneratedValue
    private Long Id;

    @Column(nullable = false)
    private String username;

    @Builder
    public User(String username){
        this.username = username;
    }

}
