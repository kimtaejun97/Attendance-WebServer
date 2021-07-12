package com.attendance.modules.beacon;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class BeaconRepositoryTest {
    @Autowired
    BeaconRepository beaconRepository;

    @Test
    public void save(){
        String beaconCode = "ASD123";
        String lectureCode= "ACD1222";

        Beacon beacon = Beacon.builder()
                .beaconCode(beaconCode)
                .lectureCode(lectureCode)
                .build();

        Beacon result =  beaconRepository.save(beacon);

        assertThat(result.getLectureCode()).isEqualTo(lectureCode);
        assertThat(result.getBeaconCode()).isEqualTo(beaconCode);
    }
}