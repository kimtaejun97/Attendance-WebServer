package com.attendance.modules.userlocation;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserLocationService {

    private final UserLocationRepository userLocationRepository;

    public void addUser(String username, String location) {

        userLocationRepository.save(
                UserLocation.builder()
                        .username(username)
                        .location(location)
                        .build()
        );

    }

    public void connectPlace(String username, String location) {
    }
}
