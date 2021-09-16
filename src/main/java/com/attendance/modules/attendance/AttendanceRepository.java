package com.attendance.modules.attendance;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
public interface AttendanceRepository extends JpaRepository<Attendance, Long> {

}
