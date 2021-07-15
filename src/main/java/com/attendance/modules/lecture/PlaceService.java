package com.attendance.modules.lecture;

import com.attendance.modules.lecture.form.PlaceForm;
import com.attendance.modules.student.UserRepository;
import com.attendance.modules.studentlecture.UserLocation;
import com.attendance.modules.studentlecture.UserLocationRepository;
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

    private final UserRepository userRepository;

    private final UserLocationRepository userLocationRepository;

    public void createPlace(PlaceForm placeForm) {

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
                .map(studentLecture ->
                        userRepository.findByUsername(studentLecture.getUsername()))
                .collect(Collectors.toList());
    }

    public List<Place> getPlacesFromUser(String username) {
        List<UserLocation> userLocations =  userLocationRepository.findByUsername(username);

        return userLocations.stream()
                .map(studentLecture ->
                        placeRepository.findByLocation(studentLecture.getLocation()))
                .collect(Collectors.toList());
    }

    public boolean isConstructor(String loacation, String nickname) {
        Place byLocation = placeRepository.findByLocation(loacation);

        return byLocation.getConstructor().equals(nickname);
    }

    public Place getPlace(String location){
        return placeRepository.findByLocation(location);
    }

    public List<Place> getPublicPlaceList() {
        return placeRepository.findByIsPublic(true);

    }
}
