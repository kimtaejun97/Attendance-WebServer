package com.attendance.modules.userplace;

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
    @JoinColumn
    private Account account;

    @ManyToOne(cascade = CascadeType.REMOVE)
    @JoinColumn
    private Place place;


}
