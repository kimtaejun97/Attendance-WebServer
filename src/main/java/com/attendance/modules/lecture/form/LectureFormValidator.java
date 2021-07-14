package com.attendance.modules.lecture.form;

import com.attendance.modules.lecture.LectureRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@RequiredArgsConstructor
@Component
public class LectureFormValidator implements Validator {

    private final LectureRepository lectureRepository;

    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.isAssignableFrom(LectureForm.class);
    }

    @Override
    public void validate(Object target, Errors errors) {
        LectureForm lectureForm =(LectureForm) target;

        if(lectureRepository.existsByLectureCode(lectureForm.getLectureCode())){
            errors.rejectValue("lectureCode","invalid.lectureCode",new Object[]{lectureForm.getLectureCode()}, "이미 존재하는 강의 코드 입니다.");
        }

    }
}
