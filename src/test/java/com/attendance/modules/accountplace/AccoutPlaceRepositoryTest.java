package com.attendance.modules.accountplace;

import com.attendance.modules.account.Account;
import com.attendance.modules.account.AccountRepository;
import com.attendance.modules.beacon.Beacon;
import com.attendance.modules.beacon.BeaconRepository;
import com.attendance.modules.place.Place;
import com.attendance.modules.place.PlaceRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
public class AccoutPlaceRepositoryTest {

    @Autowired
    AccountPlaceRepository accountPlaceRepository;

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    PlaceRepository placeRepository;
    @Autowired
    BeaconRepository beaconRepository;

    @Test
    public void save() {
        Beacon beacon = beaconRepository.save(Beacon.builder()
                .location("광주")
                .beaconCode("123df-3fsdf3-dsaf-3")
                .creator("bigave")
                .creationDate(LocalDateTime.now())
                .build());

        Account account = accountRepository.save(Account.builder()
            .username("bigave")
            .email("test@email.com")
            .password("123123123")
            .creationDate(LocalDateTime.now())
            .build());

        Place place = placeRepository.save(Place.builder()
                .alias("광주")
                .creator("bigave")
                .creationDate(LocalDateTime.now())
                .isPublic("on")
                .build());

        place.setBeacon(beacon);

        AccountPlace accountPlace = AccountPlace.builder()
                .account(account)
                .place(place)
                .build();

        AccountPlace result = accountPlaceRepository.save(accountPlace);

        assertNotNull(result);
        assertThat(result.getPlace().getBeacon().getLocation()).isEqualTo("광주");
        assertThat(result.getAccount().getUsername()).isEqualTo("bigave");

    }
}