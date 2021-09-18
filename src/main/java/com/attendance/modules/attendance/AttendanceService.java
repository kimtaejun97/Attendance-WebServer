package com.attendance.modules.attendance;

import com.attendance.modules.account.Account;
import com.attendance.modules.attendance.form.AttendanceRequestDto;
import com.attendance.modules.place.Place;
import com.attendance.modules.place.PlaceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Transactional
@Service
public class AttendanceService {

    private final PlaceService placeService;
    private final AttendanceRepository attendanceRepository;

    public void checkIn(String location, Account account) {
        AttendanceRequestDto requestDto = makeCheckInDto(location, account);
        saveAttendance(requestDto);
    }

    private void saveAttendance(AttendanceRequestDto requestDto) {
        attendanceRepository.save(new Attendance(requestDto));
    }

    private AttendanceRequestDto makeCheckInDto(String location, Account account) {
        AttendanceRequestDto requestDto = new AttendanceRequestDto();
        setPropertiesCheckIn(requestDto, location, account);
        return requestDto;
    }

    private void setPropertiesCheckIn(AttendanceRequestDto requestDto, String location, Account account) {
        requestDto.setAttendanceCode("checkIn");
        requestDto.setAttendanceDate(LocalDateTime.now());
        requestDto.setAccount(account);
        requestDto.setPlace(placeService.findByLocation(location));
    }

    public void checkOut(String location, Account account) {
        AttendanceRequestDto requestDto = makeCheckOutDto(account, location);
        saveAttendance(requestDto);
    }

    private AttendanceRequestDto makeCheckOutDto(Account account, String location) {
        AttendanceRequestDto requestDto = new AttendanceRequestDto();
        setPropertiesCheckOut(account,location ,requestDto);

        return requestDto;
    }

    private void setPropertiesCheckOut(Account account, String location, AttendanceRequestDto requestDto) {
        requestDto.setAttendanceCode("checkOut");
        requestDto.setAttendanceDate(LocalDateTime.now());
        requestDto.setAccount(account);
        requestDto.setPlace(placeService.findByLocation(location));
    }

    public boolean canCheckIn(Account account, Place place) {
        String lastCheckCode = getLastCheckCode(account, place);
        if(lastCheckCode == null){
            return true;
        }
        if(lastCheckCode.equals("checkOut")){
            return true;
        }
        return false;
    }

    private String getLastCheckCode(Account account, Place place) {
        Attendance attendance = findLastAttendance(account, place);
        if (attendance == null){
            return null;
        }
        return attendance.getAttendanceCode();
    }

    private Attendance findLastAttendance(Account account, Place place) {
        return attendanceRepository.findFirstByAccountAndPlaceOrderByAttendanceDateDesc(account, place);
    }

    public List<Attendance> getAttendances(Place place, Account account) {
        boolean isConstructor = placeService.isCreator(place.getLocation(), account);
        if(isConstructor){
            return getAllUsersAttendance(place);
        }
        return getCurrentUserAttendance(place, account);
    }

    private List<Attendance> getCurrentUserAttendance(Place place, Account account) {
        return attendanceRepository.findByAccountAndPlaceOrderByAttendanceDateDesc(account, place);
    }

    private List<Attendance> getAllUsersAttendance(Place place) {
        return attendanceRepository.findByPlaceOrderByAttendanceDateDesc(place);
    }
}
