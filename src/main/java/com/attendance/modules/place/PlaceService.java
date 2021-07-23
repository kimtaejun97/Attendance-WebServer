package com.attendance.modules.place;

import com.attendance.modules.account.Account;
import com.attendance.modules.account.AccountRepository;
import com.attendance.modules.place.form.PlaceForm;
import com.attendance.modules.userplace.UserPlace;
import com.attendance.modules.userplace.UserPlaceRepository;
import com.attendance.modules.userplace.UserPlaceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    private final UserPlaceRepository userPlaceRepository;

    public void createPlace(PlaceForm placeForm, String isPublic) {
        if(isPublic == null){
            placeForm.setIsPublic("off");
        }
        else{
            placeForm.setIsPublic(isPublic);
        }

        Account account = accountRepository.findByUsername(placeForm.getCreator());
        Place place = placeRepository.save(placeForm.toEntity());

        userPlaceRepository.save(UserPlace.builder()
                .account(account)
                .place(place)
                .build());
    }

    public List<PlaceListResponseDto> getPlaceList() {
        return placeRepository.findAll().stream()
                .map(PlaceListResponseDto::new)
                .collect(Collectors.toList());
    }

    public List<PlaceListResponseDto> getPlacesFromUser(Account account) {
        Optional<Account> byId = accountRepository.findById(account.getId());


        return byId.get().getUserPlaces().stream()
                .map(UserPlace::getPlace)
                .map(PlaceListResponseDto::new)
                .collect(Collectors.toList());


    }

    public List<String> getUsersFromPlace(Place place) {
        place = placeRepository.findByLocation(place.getLocation());
        Set<UserPlace> placeAccounts = place.getUserPlaces();

        return  placeAccounts.stream()
                .map(UserPlace::getAccount)
                .map(account -> account.getUsername())
                .collect(Collectors.toList());
    }

    public boolean isCreator(String loacation, String username) {
        Place byLocation = placeRepository.findByLocation(loacation);

        return byLocation.getCreator().equals(username);
    }

    public Place getPlace(String location){
        return placeRepository.findByLocation(location);
    }

    public List<PlaceListResponseDto> getPublicPlaceList() {

        return placeRepository.findByIsPublic("on").stream()
                .map(PlaceListResponseDto :: new)
                .collect(Collectors.toList());

    }


}
