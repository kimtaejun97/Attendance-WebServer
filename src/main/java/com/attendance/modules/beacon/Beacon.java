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

    @Id @GeneratedValue
    private Long Id;

    @Column(nullable = false, unique = true)
    private String beaconCode;

    @ManyToOne
    private Account creator;

    @Column(nullable = false, unique = true)
    private String location;

    @Column(nullable = false)
    private LocalDateTime creationDate;

    @OneToOne(cascade = CascadeType.REMOVE, mappedBy = "beacon")
    private Place place;


    public void validateEqualsToCreator(Account account) throws IllegalAccessException {
        if(!creator.equals(account)){
            throw new IllegalAccessException("접근 권한이 없습니다.");
        }
    }
}
