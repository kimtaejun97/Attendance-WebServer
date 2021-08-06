package com.attendance.modules.beacon;

import com.attendance.modules.account.Account;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.UUID;

@Component
public class BeaconFactory {

    @Autowired
    BeaconRepository beaconRepository;

    public Beacon createNewBeacon(String location, Account account, String beaconCode){
        return beaconRepository.save(Beacon.builder()
                .location(location)
                .beaconCode(beaconCode==null? UUID.randomUUID().toString():beaconCode)
                .creator(account)
                .creationDate(LocalDateTime.now())
                .build());


    }
}
