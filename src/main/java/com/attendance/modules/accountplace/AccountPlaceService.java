package com.attendance.modules.accountplace;

import com.attendance.modules.account.Account;
import com.attendance.modules.account.AccountRepository;
import com.attendance.modules.account.AccountService;
import com.attendance.modules.place.Place;
import com.attendance.modules.place.PlaceRepository;
import com.attendance.modules.place.PlaceService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@RequiredArgsConstructor
@Service
public class AccountPlaceService {

    private final AccountService accountService;
    private final AccountPlaceRepository accountPlaceRepository;
    private PlaceService placeService;

    @Autowired
    public void setPlaceService(PlaceService placeService) {
        this.placeService = placeService;
    }

    public void connectAccountPlace(Account creator, Place place) {
            accountPlaceRepository.save(new AccountPlace(creator,place));
    }

    public void disconnect(Account account, Place place) {
        AccountPlace accountPlace = accountPlaceRepository.findByAccountAndPlace(account, place);
        accountPlaceRepository.delete(accountPlace);
    }

    public boolean isEnrolled(Account account, Place place) {
        return accountPlaceRepository.existsByAccountUsernameAndPlaceId(account.getUsername(), place.getId());
    }

    public void leave(Account account, String location) {
        Place place = placeService.findByLocation(location);
        disconnect(account, place);
    }

    public void removeUser(String targetUsername, Place place) {
        Account targetAccount = accountService.findByUsername(targetUsername);
        disconnect(targetAccount, place);
    }
}
