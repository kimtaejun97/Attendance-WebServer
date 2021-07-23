package com.attendance.modules.userplace;

import com.attendance.modules.account.Account;
import com.attendance.modules.place.Place;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly = true)
public interface UserPlaceRepository extends JpaRepository<UserPlace, Long> {

    boolean existsByAccountAndPlace(Account account, Place place);

    UserPlace findByAccountIdAndPlaceLocation(Long id, String location);

    boolean existsByAccountIdAndPlaceLocation(Long id, String location);
}
