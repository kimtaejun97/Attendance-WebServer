package com.attendance.modules.userplace;

import com.attendance.modules.account.Account;
import com.attendance.modules.account.AccountRepository;
import com.attendance.modules.place.Place;
import com.attendance.modules.place.PlaceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@RequiredArgsConstructor
@Service
public class UserPlaceService {

    private final AccountRepository accountRepository;
    private final PlaceRepository placeRepository;

    private final UserPlaceRepository userPlaceRepository;

    public void connectUserPlace(String username, String location) {
        Account account= accountRepository.findByUsername(username);
        Place place = placeRepository.findByLocation(location);

            userPlaceRepository.save(
                    UserPlace.builder()
                            .account(account)
                            .place(place)
                            .build());
    }

}
