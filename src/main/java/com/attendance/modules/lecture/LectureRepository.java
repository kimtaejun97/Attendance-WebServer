package com.attendance.modules.lecture;

import com.attendance.modules.lecture.Lecture;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LectureRepository extends JpaRepository<Lecture, String> {
    boolean existsByLectureCode(String lectureCode);

    Lecture findByLectureCode(String lectureCode);
}
