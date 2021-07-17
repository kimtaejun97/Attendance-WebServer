package com.attendance.modules.place;

import com.attendance.modules.account.AccountRepository;
import com.attendance.modules.place.form.PlaceForm;
import com.attendance.modules.userplace.UserPlace;
import com.attendance.modules.userplace.UserPlaceRepository;
import com.attendance.modules.userplace.UserPlaceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;
@Transactional
@RequiredArgsConstructor
@Service
public class PlaceService {

    private final PlaceRepository placeRepository;

    private AccountRepository accountRepository;

    private final UserPlaceRepository userPlaceRepository;

    public void createPlace(PlaceForm placeForm, String isPublic) {
        if(isPublic == null){
            placeForm.setIsPublic("off");
        }
        else{
            placeForm.setIsPublic(isPublic);
        }

        Place place = placeForm.toEntity();

        placeRepository.save(placeForm.toEntity());
        userPlaceRepository.save(UserPlace.builder()
                .username(placeForm.getCreator())
                .location(placeForm.getLocation())
                .build());
    }

    public List<PlaceListResponseDto> getPlaceList() {
        return placeRepository.findAll().stream()
                .map(PlaceListResponseDto::new)
                .collect(Collectors.toList());
    }

    public List<PlaceListResponseDto> getPlacesFromUser(String username) {
        List<UserPlace> userPlaces = userPlaceRepository.findByUsername(username);

        return userPlaces.stream()
                .map(userPlace ->
                        placeRepository.findByLocation(userPlace.getLocation()))
                .map(PlaceListResponseDto::new)
                .collect(Collectors.toList());


    }

    public List<String> getUsersFromPlace(String location) {
        List<UserPlace> userPlaces = userPlaceRepository.findAllByLocation(location);

       return  userPlaces.stream()
                .map(userLocation ->
                        accountRepository.findByUsernameReturnUsername(userLocation.getUsername()))
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
