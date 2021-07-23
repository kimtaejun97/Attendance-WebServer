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

    private final UserPlaceService userPlaceService;

    private final AccountRepository accountRepository;

    public void createPlace(PlaceForm placeForm, String isPublic) {
        if(isPublic == null){
            placeForm.setIsPublic("off");
        }
        else{
            placeForm.setIsPublic(isPublic);
        }

        placeRepository.save(placeForm.toEntity());
        userPlaceService.connectUserPlace(placeForm.getCreator(), placeForm.getLocation());

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

        Set<UserPlace> placeAccounts = place.getUserPlaces();

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
                .map(PlaceListResponseDto :: new)
                .collect(Collectors.toList());

    }


}
