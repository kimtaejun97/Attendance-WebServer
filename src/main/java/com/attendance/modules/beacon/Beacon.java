package com.attendance.modules.beacon;

import com.attendance.modules.account.Account;
import com.attendance.modules.place.Place;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

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

    @Column(nullable = false)
    private LocalDateTime creationDate;

    @OneToOne(cascade = CascadeType.REMOVE, mappedBy = "beacon")
    private Place place;

    @ManyToOne(cascade = CascadeType.REMOVE)
    private Account account;





}
