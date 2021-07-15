package com.attendance.modules.userplace;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserLocationRepository extends JpaRepository<UserLocation, Long> {
    List<UserLocation> findAllByLocation(String location);

    boolean existsByLocationAndUsername(String location, String username);

    List<UserLocation> findByUsername(String username);


}
