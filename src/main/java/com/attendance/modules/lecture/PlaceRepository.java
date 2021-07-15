package com.attendance.modules.lecture;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PlaceRepository extends JpaRepository<Place, String> {
    boolean existsByLocation(String location);

    Place findByLocation(String location);

    List<Place> findByIsPublic(Boolean isPublic);

}
