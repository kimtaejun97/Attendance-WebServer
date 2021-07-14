package com.attendance.modules.studentlecture;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface StudentLectureRepository extends JpaRepository<StudentLecture, Long> {
    List<StudentLecture> findAllByLectureCode(String lectureCode);

    boolean existsByLectureCodeAndStudentName(String lectureCode, String studentName);

    List<StudentLecture> findByStudentName(String studentName);


}
