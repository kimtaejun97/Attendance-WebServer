package com.attendance.modules.user;

import com.attendance.modules.userplace.UserLocation;
import com.attendance.modules.userplace.UserLocationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UsersService {

    private final UsersRepository usersRepository;

    private final UserLocationRepository userLocationRepository;

    public void addUser(String username, String location) {
        Users users = Users.builder()
                .username(username)
                .build();

        Users newUsers = usersRepository.save(users);

        userLocationRepository.save(
                UserLocation.builder()
                        .username(newUsers.getUsername())
                        .location(location)
                        .build()
        );



    }
}
