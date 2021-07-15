package com.attendance.modules.student;

import com.attendance.modules.studentlecture.UserLocation;
import com.attendance.modules.studentlecture.UserLocationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;

    private final UserLocationRepository userLocationRepository;

    public void addUser(String username, String location) {
        User user = User.builder()
                .username(username)
                .build();

        User newUser = userRepository.save(user);

        userLocationRepository.save(
                UserLocation.builder()
                        .username(newUser.getUsername())
                        .location(location)
                        .build()
        );



    }
}
