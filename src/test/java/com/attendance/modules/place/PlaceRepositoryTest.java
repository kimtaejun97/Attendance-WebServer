package com.attendance.modules.place;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class PlaceRepositoryTest {

    @Autowired
    PlaceRepository placeRepository;

    @Test
    public void save(){

        String location = "광주 동구 10-2";
        String alias = "메가커피";
        String constructor = "bigave";

        Place place = Place.builder()
                .location(location)
                .alias(alias)
                .constructor(constructor)
                .build();

        Place result = placeRepository.save(place);

        assertThat(result.getLocation()).isEqualTo(location);
        assertThat(result.getAlias()).isEqualTo(alias);
        assertThat(result.getConstructor()).isEqualTo(constructor);

    }
}