package com.attendance.modules.userlocation;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
public class UserLocation {

    @Id @GeneratedValue
    private Long Id;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String location;

    @Builder
    public UserLocation(String username, String location){
        this.username = username;
        this.location = location;

    }
}
