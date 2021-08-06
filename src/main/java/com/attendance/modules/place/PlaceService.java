package com.attendance.modules.place;

import com.attendance.modules.account.Account;
import com.attendance.modules.account.AccountRepository;
import com.attendance.modules.beacon.Beacon;
import com.attendance.modules.beacon.BeaconRepository;
import com.attendance.modules.place.form.PlaceForm;
import com.attendance.modules.place.form.PlaceListResponseDto;
import com.attendance.modules.accountplace.AccountPlace;
import com.attendance.modules.accountplace.AccountPlaceService;
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

    private final AccountPlaceService accountPlaceService;

    private final AccountRepository accountRepository;
    private final BeaconRepository beaconRepository;
    private final ModelMapper modelMapper;

    public void createPlace(PlaceForm placeForm, String isPublic) {
        if(isPublic == null){
            placeForm.setIsPublic("off");
        }
        else{
            placeForm.setIsPublic(isPublic);
        }
        placeForm.setCreationDate(LocalDateTime.now());

        Place place = placeRepository.save(modelMapper.map(placeForm, Place.class));
        Beacon beacon =  beaconRepository.findByLocation(placeForm.getLocation());

        place.setBeacon(beacon);

        accountPlaceService.connectAccountPlace(placeForm.getCreator(), place);



    }

    public List<PlaceListResponseDto> getAllPlaces() {
        return placeRepository.findAll().stream()
                .map(p-> modelMapper.map(p,PlaceListResponseDto.class))
                .collect(Collectors.toList());


//        return placeRepository.findAll().stream()
//                .map(p->{
//                    String location = beaconRepository.findByPlace(p).getLocation();
//                    PlaceListResponseDto map = modelMapper.map(p, PlaceListResponseDto.class);
//                    map.setLocation(location);
//                    return map;
//                }).collect(Collectors.toList());

    }

    public List<PlaceListResponseDto> getPlacesByAccount(Account account) {
        Optional<Account> byId = accountRepository.findById(account.getUsername());

        return byId.get().getAccountPlaces().stream()
                .map(AccountPlace::getPlace)
                .map(p-> modelMapper.map(p,PlaceListResponseDto.class))
                .collect(Collectors.toList());
    }

    public List<String> getUsersByPlace(Place place) {

        Set<AccountPlace> placeAccounts = place.getAccountPlaces();

        return  placeAccounts.stream()
                .map(placeAccount -> placeAccount.getAccount().getUsername())
                .collect(Collectors.toList());
    }

    public boolean isCreator(String location, String username) {
        Place byLocation = placeRepository.findByLocation(location);

        return byLocation.getCreator().equals(username);
    }

    public Place getPlace(String location){
        return placeRepository.findByLocation(location);
    }

    public List<PlaceListResponseDto> getPublicPlaceList() {

        return placeRepository.findByIsPublic("on").stream()
                .map(p-> modelMapper.map(p,PlaceListResponseDto.class))
                .collect(Collectors.toList());

    }


}
