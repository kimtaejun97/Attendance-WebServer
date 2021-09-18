package com.attendance.modules.place;

import com.attendance.modules.account.Account;
import com.attendance.modules.account.AccountRepository;
import com.attendance.modules.account.AccountService;
import com.attendance.modules.accountplace.AccountPlaceRepository;
import com.attendance.modules.beacon.Beacon;
import com.attendance.modules.beacon.BeaconRepository;
import com.attendance.modules.place.form.PlaceForm;
import com.attendance.modules.place.form.PlaceListResponseDto;
import com.attendance.modules.accountplace.AccountPlace;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Transactional
@RequiredArgsConstructor
@Service
public class PlaceService {
    private final PlaceRepository placeRepository;
    private final AccountRepository accountRepository;
    private final BeaconRepository beaconRepository;
    private final ModelMapper modelMapper;
    private final AccountService accountService;
    private final AccountPlaceRepository accountPlaceRepository;

    public void createPlace(PlaceForm placeForm, Account account) {
        setProperties(placeForm, account);
        saveAndCreatorRegistration(placeForm);
    }

    private void saveAndCreatorRegistration(PlaceForm placeForm) {
        Place place = placeRepository.save(modelMapper.map(placeForm, Place.class));
        connectAccountPlace(placeForm.getCreator(), place);
    }

    private void setProperties(PlaceForm placeForm, Account account) {
        placeForm.setCreator(account);
        placeForm.setCreationDate(LocalDateTime.now());
        setBeacon(placeForm);
    }

    private void setBeacon(PlaceForm placeForm) {
        Beacon beacon =  beaconRepository.findByLocation(placeForm.getLocation());
        placeForm.setBeacon(beacon);
    }

    public List<PlaceListResponseDto> getPlacesForAdmin() {
        return placeRepository.findAll().stream()
                .map(p-> modelMapper.map(p,PlaceListResponseDto.class))
                .collect(Collectors.toList());

    }

    public List<PlaceListResponseDto> getPlacesByAccount(Account account) {
        Account byUsername = accountRepository.findByUsername(account.getUsername());

        return byUsername.getAccountPlaces().stream()
                .map(AccountPlace::getPlace)
                .map(p-> modelMapper.map(p,PlaceListResponseDto.class))
                .collect(Collectors.toList());
    }

    public List<String> getUsersByPlace(Place place) {
        return  place.getAccountPlaces().stream()
                .map(placeAccount -> placeAccount.getAccount().getUsername())
                .collect(Collectors.toList());
    }

    public boolean isCreator(String location, Account CurrentAccount) {
        Place byLocation = placeRepository.findByLocation(location);
        return byLocation.getCreator().equals(CurrentAccount);
    }

    public Place getPlace(String location){
        return placeRepository.findByLocation(location);
    }

    public List<PlaceListResponseDto> getPublicPlaceList() {
        return placeRepository.findByIsPublicOrderByCreationDateDesc(true).stream()
                .map(p-> modelMapper.map(p,PlaceListResponseDto.class))
                .collect(Collectors.toList());
    }


    public Place findByLocation(String location) {
        Place place = placeRepository.findByLocation(location);
        if(place == null){
            throw new IllegalArgumentException("존재하지 않는 위치 입니다.");
        }
        return place;
    }

    public void connectAccountPlace(Account creator, Place place) {
        accountPlaceRepository.save(new AccountPlace(creator,place));
    }

    public void disconnect(Account account, Place place) {
        AccountPlace accountPlace = accountPlaceRepository.findByAccountAndPlace(account, place);
        accountPlaceRepository.delete(accountPlace);
    }

    public boolean isEnrolled(Account account, Place place) {
        return accountPlaceRepository.existsByAccountUsernameAndPlaceId(account.getUsername(), place.getId());
    }

    public void leave(Account account, String location) {
        Place place = findByLocation(location);
        disconnect(account, place);
    }

    public void removeUser(String targetUsername, Place place) {
        Account targetAccount = accountService.findByUsername(targetUsername);
        disconnect(targetAccount, place);
    }

    public void remove(String location) {
        Place place = findByLocation(location);
        placeRepository.delete(place);
    }

    public void addUser(String username, Place place) {
        Account account = accountService.findByUsername(username);
        connectAccountPlace(account, place);
    }

    public Place findByBeaconCode(String beaconCode) {
        return placeRepository.findByBeaconCode(beaconCode);
    }
}
