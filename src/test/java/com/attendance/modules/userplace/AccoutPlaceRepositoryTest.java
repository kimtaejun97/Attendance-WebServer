package com.attendance.modules.userplace;

import com.attendance.modules.account.Account;
import com.attendance.modules.account.AccountRepository;
import com.attendance.modules.place.Place;
import com.attendance.modules.place.PlaceRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

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

    @Test
    public void save() {
        Account account = accountRepository.save(Account.builder()
        .username("bigave")
        .email("test@email.com")
        .password("123123123")
        .build());
        Place place = placeRepository.save(Place.builder()
                .location("광주")
                .alias("광주")
                .creator("bigave")
                .build());

        AccountPlace accountPlace = AccountPlace.builder()
                .account(account)
                .place(place)
                .build();

        AccountPlace result = accountPlaceRepository.save(accountPlace);

        assertNotNull(result);
        assertThat(result.getPlace().getLocation()).isEqualTo("광주");
        assertThat(result.getAccount().getUsername()).isEqualTo("bigave");

    }
}