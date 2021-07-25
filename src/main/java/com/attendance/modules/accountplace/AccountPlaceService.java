package com.attendance.modules.accountplace;

import com.attendance.modules.account.Account;
import com.attendance.modules.account.AccountRepository;
import com.attendance.modules.place.Place;
import com.attendance.modules.place.PlaceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@RequiredArgsConstructor
@Service
public class AccountPlaceService {

    private final AccountRepository accountRepository;
    private final PlaceRepository placeRepository;

    private final AccountPlaceRepository accountPlaceRepository;

    public void connectUserPlace(String username, Place place) {
        Account account= accountRepository.findByUsername(username);

            accountPlaceRepository.save(
                    AccountPlace.builder()
                            .account(account)
                            .place(place)
                            .build());
    }

}
