package com.attendance.modules.place;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly = true)
public interface PlaceRepository extends JpaRepository<Place, String> {
    boolean existsByLocation(String location);

    Place findByLocation(String location);

    List<Place> findByIsPublic(String isPublic);

    List<Place> findByCreator(String creator);

}
