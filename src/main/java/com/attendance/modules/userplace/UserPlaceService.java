package com.attendance.modules.userplace;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@RequiredArgsConstructor
@Service
public class UserPlaceService {

    private final UserPlaceRepository userPlaceRepository;

    public void connectUserPlace(String username, String location) {

            userPlaceRepository.save(
                    UserPlace.builder()
                            .username(username)
                            .location(location)
                            .build());
    }

}
