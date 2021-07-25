package com.attendance.modules.beacon;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly = true)
public interface BeaconRepository extends JpaRepository<Beacon, String> {
    boolean existsByLocation(String location);

    boolean existsByBeaconCode(String beaconCode);

    Beacon findByLocation(String location);

    List<Beacon> findByCreator(String username);
}
