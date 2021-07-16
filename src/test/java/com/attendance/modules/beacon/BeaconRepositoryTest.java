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
        String beaconCode = "ASD123-1233aasdf-fadsf113";
        String location= "광주 동구";
        String constructor = "bigave";

        Beacon beacon = Beacon.builder()
                .beaconCode(beaconCode)
                .location(location)
                .creator(constructor)
                .build();

        Beacon result =  beaconRepository.save(beacon);



        assertThat(result.getLocation()).isEqualTo(location);
        assertThat(result.getBeaconCode()).isEqualTo(beaconCode);
        assertThat(result.getCreator()).isEqualTo(constructor);
    }
}