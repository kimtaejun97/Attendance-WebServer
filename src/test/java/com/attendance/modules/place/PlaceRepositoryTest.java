package com.attendance.modules.place;

import com.attendance.modules.beacon.Beacon;
import com.attendance.modules.beacon.BeaconRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class PlaceRepositoryTest {

    @Autowired
    PlaceRepository placeRepository;

    @Autowired
    BeaconRepository beaconRepository;

    @Test
    public void save(){
        Beacon beacon = beaconRepository.save(Beacon.builder()
                .location("광주")
                .beaconCode("123df-3fsdf3-dsaf-3")
                .creator("bigave")
                .creationDate(LocalDateTime.now())
                .build());

        String alias = "메가커피";
        String creator = "bigave";

        Place place = Place.builder()
                .alias(alias)
                .creator(creator)
                .creationDate(LocalDateTime.now())
                .isPublic("on")
                .build();
        place.setBeacon(beacon);

        Place result = placeRepository.save(place);

        assertThat(result.getAlias()).isEqualTo(alias);
        assertThat(result.getCreator()).isEqualTo(creator);
        assertThat(result.getBeacon()).isEqualTo(beacon);

    }
}