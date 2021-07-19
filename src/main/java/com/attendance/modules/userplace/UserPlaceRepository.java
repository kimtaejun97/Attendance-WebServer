package com.attendance.modules.userplace;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly = true)
public interface UserPlaceRepository extends JpaRepository<UserPlace, Long> {
    List<UserPlace> findAllByLocation(String location);

    boolean existsByLocationAndUsername(String location, String username);

    List<UserPlace> findByUsername(String username);


}
