package com.attendance.modules.student;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface StudentRepository extends JpaRepository<Student, Long> {

    @Query("select S.studentName from Student S where S.studentName like ?1")
    String findByStudentName(String studentName);
}
