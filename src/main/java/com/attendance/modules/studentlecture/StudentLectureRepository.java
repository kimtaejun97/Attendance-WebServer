package com.attendance.modules.studentlecture;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StudentLectureRepository extends JpaRepository<StudentLecture, Long> {
    List<StudentLecture> findAllByLectureCode(String lectureCode);
}
