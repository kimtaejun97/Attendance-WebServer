package com.attendance.modules.place;

import com.attendance.modules.beacon.Beacon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly = true)
public interface PlaceRepository extends JpaRepository<Place, Long> {

    @Query("select count(P) > 0 From Place P where P.beacon = (select B FROM Beacon B where B.location like ?1)")
    boolean existsByLocation(String location);

    @Query("select P From Place P where P.beacon = (select B FROM Beacon B where B.location like ?1)")
    Place findByLocation(String location);

    List<Place> findByIsPublicOrderByCreationDateDesc(boolean isPublic);


    @Query("select P from Place P where P.beacon.beaconCode like ?1")
    Place findByBeaconCode(String beaconCode);
}
