package com.attendance.modules.userplace;

import com.attendance.modules.account.Account;
import com.attendance.modules.place.Place;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
public interface AccountPlaceRepository extends JpaRepository<AccountPlace, Long> {

    boolean existsByAccountAndPlace(Account account, Place place);

    AccountPlace findByAccountIdAndPlaceLocation(Long id, String location);

    boolean existsByAccountIdAndPlaceLocation(Long id, String location);
}