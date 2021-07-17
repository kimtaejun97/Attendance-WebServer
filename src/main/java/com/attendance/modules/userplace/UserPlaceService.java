package com.attendance.modules.userplace;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserPlaceService {

    private final UserPlaceRepository userPlaceRepository;

    public String connectUserPlace(String username, String location) {
        if(!userPlaceRepository.existsByLocationAndUsername(location,username)){
            userPlaceRepository.save(
                    UserPlace.builder()
                            .username(username)
                            .location(location)
                            .build()
            );
            return "S";
        }
        return "F";

    }

}
