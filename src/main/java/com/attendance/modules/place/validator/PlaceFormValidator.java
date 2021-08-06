package com.attendance.modules.place.validator;

import com.attendance.modules.beacon.Beacon;
import com.attendance.modules.beacon.BeaconRepository;
import com.attendance.modules.place.PlaceRepository;
import com.attendance.modules.place.form.PlaceForm;
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
        Beacon beacon =  beaconRepository.findByLocation(placeForm.getLocation());

        if(placeRepository.existsByLocation(placeForm.getLocation())){
            errors.rejectValue("location","invalid.locatiaon",new Object[]{placeForm.getLocation()}, "이미 생성되어 있는 장소 입니다.");
        }

        if(beacon == null){
            errors.rejectValue("location","invalid.locatiaon",new Object[]{placeForm.getLocation()}, "존재하지 않는 비콘 위치 입니다.");
        }
        else{
            if(!beacon.getCreator().equals(placeForm.getCreator())){
                errors.rejectValue("location","invalid.locatiaon",new Object[]{placeForm.getLocation()}, "접근할 수 없는 비콘 입니다.");
            }
        }



    }
}
