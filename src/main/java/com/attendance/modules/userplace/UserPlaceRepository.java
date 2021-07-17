package com.attendance.modules.userplace;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserPlaceRepository extends JpaRepository<UserPlace, Long> {
    List<UserPlace> findAllByLocation(String location);

    boolean existsByLocationAndUsername(String location, String username);

    List<UserPlace> findByUsername(String username);


}
