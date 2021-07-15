package com.attendance.modules.userplace;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class UsersPlaceRepositoryTest {

    @Autowired
    UserLocationRepository userLocationRepository;

    @Test
    public void save() {
        String location = "광주 동구...";
        String username = "bigave";

        UserLocation userLocation = UserLocation.builder()
                .location(location)
                .username(username)
                .build();

        UserLocation result = userLocationRepository.save(userLocation);

        assertThat(result.getLocation()).isEqualTo(location);
        assertThat(result.getUsername()).isEqualTo(username);

    }
}