package com.attendance.modules.place;

import com.attendance.modules.account.Account;
import com.attendance.modules.beacon.Beacon;
import com.attendance.modules.beacon.BeaconRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.UUID;

@Component
public class PlaceFactory {

   @Autowired
   BeaconRepository beaconRepository;
   @Autowired
   PlaceRepository placeRepository;

    public Place createNewPlace(String location, Account account){
        Beacon beacon = beaconRepository.save(Beacon.builder()
                .location(location)
                .beaconCode(UUID.randomUUID().toString())
                .creator(account)
                .creationDate(LocalDateTime.now())
                .build());

        Place place = placeRepository.save(
                Place.builder()
                        .alias(location)
                        .creator(account)
                        .isPublic(true)
                        .creationDate(LocalDateTime.now())
                        .beacon(beacon)
                        .build());

        beacon.setPlace(place);

        return place;


    }
}
