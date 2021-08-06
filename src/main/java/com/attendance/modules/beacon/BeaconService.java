package com.attendance.modules.beacon;

import com.attendance.modules.account.Account;
import com.attendance.modules.account.AccountRepository;
import com.attendance.modules.beacon.form.BeaconForm;
import com.attendance.modules.place.PlaceRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Transactional
@RequiredArgsConstructor
@Service
public class BeaconService {

    private final BeaconRepository beaconRepository;
    private final AccountRepository accountRepository;
    private final ModelMapper modelMapper;
    private final PlaceRepository placeRepository;



    public void addBeacon(BeaconForm beaconForm) {
        Account account = accountRepository.findByUsername(beaconForm.getCreatorName());
        beaconForm.setCreationDate(LocalDateTime.now());
        beaconForm.setCreator(account);

        Beacon beacon = beaconRepository.save(modelMapper.map(beaconForm, Beacon.class));
        account.getBeacons().add(beacon);

    }

    public Set<Beacon> getBeaconByAccount(String username) {
        Account account = accountRepository.findByUsername(username);

        return account.getBeacons();


    }

    public void removeBeacon(Account account, Beacon beacon) {

        account.getBeacons().remove(beacon);
        beaconRepository.delete(beacon);

    }

}
