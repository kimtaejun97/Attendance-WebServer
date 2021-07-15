package com.attendance.modules.lecture;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PlaceRepository extends JpaRepository<Place, String> {
    boolean existsByLocation(String location);

    Place findByLocation(String location);
}
