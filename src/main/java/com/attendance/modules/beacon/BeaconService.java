package com.attendance.modules.beacon;

import com.attendance.modules.account.Account;
import com.attendance.modules.account.AccountRepository;
import com.attendance.modules.beacon.form.BeaconForm;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@RequiredArgsConstructor
@Service
public class BeaconService {

    private final BeaconRepository beaconRepository;
    private final AccountRepository accountRepository;
    private final ModelMapper modelMapper;


    public void addBeacon(BeaconForm beaconForm) {

        Beacon beacon = beaconRepository.save(modelMapper.map(beaconForm, Beacon.class));
        Account account = accountRepository.findByUsername(beaconForm.getCreator());
        account.getBeacons().add(beacon);

    }
}
