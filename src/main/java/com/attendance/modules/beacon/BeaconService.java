package com.attendance.modules.beacon;

import com.attendance.modules.account.Account;
import com.attendance.modules.account.AccountRepository;
import com.attendance.modules.beacon.form.BeaconForm;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Transactional
@RequiredArgsConstructor
@Service
public class BeaconService {

    private final BeaconRepository beaconRepository;
    private final AccountRepository accountRepository;
    private final ModelMapper modelMapper;



    public void addBeacon(BeaconForm beaconForm) {
        Account account = accountRepository.findByUsername(beaconForm.getCreatorName());
        setCreationTimeAndAccount(beaconForm, account);
        saveBeacon(beaconForm, account);
    }

    private void saveBeacon(BeaconForm beaconForm, Account account) {
        Beacon beacon = beaconRepository.save(modelMapper.map(beaconForm, Beacon.class));
        account.getBeacons().add(beacon);
    }

    private void setCreationTimeAndAccount(BeaconForm beaconForm, Account account) {
        beaconForm.setCreationDate(LocalDateTime.now());
        beaconForm.setCreator(account);
    }

    public Set<Beacon> getBeaconByAccount(String username) {
        Account account = accountRepository.findByUsername(username);
        return account.getBeacons();
    }

    public void removeBeacon(Account account, Beacon beacon) {
        account.getBeacons().remove(beacon);
        beaconRepository.delete(beacon);
    }

    public Beacon findByLocation(String location) {
        Beacon beacon =  beaconRepository.findByLocation(location);
        if(beacon == null){
            throw new IllegalArgumentException("존재하지 않는 위치 입니다.");
        }
        return beacon;
    }

    public BeaconForm initBeaconForm(Account account) {
        BeaconForm beaconForm = new BeaconForm();
        //TODO 나중에 비콘 수신하는걸로 변경.
        setProperties(account, beaconForm);

        return beaconForm;
    }

    private void setProperties(Account account, BeaconForm beaconForm) {
        beaconForm.setBeaconCode(UUID.randomUUID().toString());
        beaconForm.setCreatorName(account.getUsername());
    }
}
