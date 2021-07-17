package com.attendance.modules.userplace;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class UsersPlaceRepositoryTest {

    @Autowired
    UserPlaceRepository userPlaceRepository;

    @Test
    public void save() {
        String location = "광주 동구...";
        String username = "bigave";

        UserPlace userPlace = UserPlace.builder()
                .location(location)
                .username(username)
                .build();

        UserPlace result = userPlaceRepository.save(userPlace);

        assertThat(result.getLocation()).isEqualTo(location);
        assertThat(result.getUsername()).isEqualTo(username);

    }
}