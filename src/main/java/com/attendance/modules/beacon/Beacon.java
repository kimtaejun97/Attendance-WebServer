package com.attendance.modules.beacon;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Getter
@NoArgsConstructor
@Entity
public class Beacon {

    @Id
    private String beaconCode;

    @Column(nullable = false)
    private String lectureCode;

    @Builder
    public Beacon(String beaconCode, String lectureCode){
        this.beaconCode = beaconCode;
        this.lectureCode = lectureCode;
    }

}
