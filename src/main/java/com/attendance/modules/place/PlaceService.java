package com.attendance.modules.place;

import com.attendance.modules.account.AccountRepository;
import com.attendance.modules.place.form.PlaceForm;
import com.attendance.modules.userlocation.UserLocation;
import com.attendance.modules.userlocation.UserLocationRepository;
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

    private final UserLocationRepository userLocationRepository;

    public void createPlace(PlaceForm placeForm, String isPublic) {
        if(isPublic == null){
            placeForm.setIsPublic("off");
        }
        else{
            placeForm.setIsPublic(isPublic);
        }

        Place place = placeForm.toEntity();

        placeRepository.save(placeForm.toEntity());


    }

    public List<PlaceListResponseDto> getPlaceList() {
        return placeRepository.findAll().stream()
                .map(PlaceListResponseDto::new)
                .collect(Collectors.toList());
    }

    public List<String> getUsersFromPlace(String location) {
        List<UserLocation> userLocations = userLocationRepository.findAllByLocation(location);
       return  userLocations.stream()
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

    public List<Place> getPublicPlaceList() {
        return placeRepository.findByIsPublic("on");

    }
}
