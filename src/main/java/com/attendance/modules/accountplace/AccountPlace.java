package com.attendance.modules.accountplace;

import com.attendance.modules.account.Account;
import com.attendance.modules.place.Place;
import lombok.*;

import javax.persistence.*;

@Getter @Setter
@Builder
@NoArgsConstructor @AllArgsConstructor
@Entity @EqualsAndHashCode(of = "Id")
public class AccountPlace {

    @Id @GeneratedValue
    private Long Id;

    @ManyToOne
    private Account account;

    @ManyToOne
    private Place place;


}
