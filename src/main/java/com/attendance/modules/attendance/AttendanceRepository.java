package com.attendance.modules.attendance;

import com.attendance.modules.account.Account;
import com.attendance.modules.place.Place;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly = true)
public interface AttendanceRepository extends JpaRepository<Attendance, Long> {

    Attendance findFirstByAccountAndPlaceOrderByAttendanceDateDesc(Account account, Place place);

    List<Attendance> findByPlaceOrderByAttendanceDateDesc(Place place);

    List<Attendance> findByAccountAndPlaceOrderByAttendanceDateDesc(Account account, Place place);
}
