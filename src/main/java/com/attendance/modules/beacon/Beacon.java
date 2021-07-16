package com.attendance.modules.beacon;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Getter @Setter @Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Beacon {

    @Id
    private String beaconCode;

    @Column(nullable = false, unique = true)
    private String location;

    @Column(nullable = false)
    private String creator;





}
