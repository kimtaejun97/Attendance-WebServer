package com.attendance.modules.userplace;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
public class UserPlace {

    @Id @GeneratedValue
    private Long Id;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String location;

    @Builder
    public UserPlace(String username, String location){
        this.username = username;
        this.location = location;

    }
}
