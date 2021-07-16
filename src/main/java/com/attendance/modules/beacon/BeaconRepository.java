package com.attendance.modules.beacon;

import org.springframework.data.jpa.repository.JpaRepository;

public interface BeaconRepository extends JpaRepository<Beacon, String> {
    boolean existsByLocation(String location);

    boolean existsByBeaconCode(String beaconCode);
}
