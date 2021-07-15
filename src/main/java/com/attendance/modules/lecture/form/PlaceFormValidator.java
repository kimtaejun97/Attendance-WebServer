package com.attendance.modules.lecture.form;

import com.attendance.modules.beacon.BeaconRepository;
import com.attendance.modules.lecture.PlaceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@RequiredArgsConstructor
@Component
public class PlaceFormValidator implements Validator {

    private final PlaceRepository placeRepository;
    private final BeaconRepository beaconRepository;

    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.isAssignableFrom(PlaceForm.class);
    }

    @Override
    public void validate(Object target, Errors errors) {
        PlaceForm placeForm =(PlaceForm) target;

        if(placeRepository.existsByLocation(placeForm.getLocation())){
            errors.rejectValue("location","invalid.locatiaon",new Object[]{placeForm.getLocation()}, "이미 생성되어 있는 장소 입니다.");
        }

        if(!beaconRepository.existsByLocation(placeForm.getLocation())){
            errors.rejectValue("location","invalid.locatiaon",new Object[]{placeForm.getLocation()}, "존재하지 않는 비콘위치 입니다.");
        }
    }
}
