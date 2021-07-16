package com.attendance.modules.place;

import com.attendance.modules.userplace.UserLocation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PlaceRepository extends JpaRepository<Place, String> {
    boolean existsByLocation(String location);

    Place findByLocation(String location);

    List<Place> findByIsPublic(String isPublic);

    List<Place> findByCreator(String creator);
}
