package com.attendance.modules.beacon.validator;

import com.attendance.modules.beacon.BeaconRepository;
import com.attendance.modules.beacon.form.BeaconForm;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@RequiredArgsConstructor
@Component
public class BeaconFormValidator implements Validator {

    private final BeaconRepository beaconRepository;


    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.isAssignableFrom(BeaconForm.class);
    }

    @Override
    public void validate(Object target, Errors errors) {
        BeaconForm beaconForm =(BeaconForm) target;

        if(beaconRepository.existsByLocation(beaconForm.getLocation())){
            errors.rejectValue("location","invalid.location",new Object[]{beaconForm.getLocation()}, "이미 사용중인 위치명 입니다.");
        }
        if(beaconRepository.existsByBeaconCode(beaconForm.getBeaconCode())){
            errors.rejectValue("beaconCode","invalid.beaconCode",new Object[]{beaconForm.getBeaconCode()}, "이미 등록된 비콘코드 입니다.");
        }
        if(beaconForm.getBeaconCode() == ""){
            errors.rejectValue("beaconCode","invalid.beaconCode",new Object[]{beaconForm.getBeaconCode()}, "비콘이 인식되지 않았습니다.");
        }

    }
}
