package com.attendance.modules.studentlecture;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface StudentLectureRepository extends JpaRepository<StudentLecture, Long> {
    List<StudentLecture> findAllByLectureCode(String lectureCode);

    @Query("SELECT new java.lang.Boolean(count(*) >0) FROM StudentLecture S WHERE S.studentName like ?2 and S.lectureCode like ?1")
    boolean existsStudent(String lectureCode, String studentName);
}
