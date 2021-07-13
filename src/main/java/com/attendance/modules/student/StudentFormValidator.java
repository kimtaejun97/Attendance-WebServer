package com.attendance.modules.student;

import com.attendance.modules.account.AccountRepository;
import com.attendance.modules.lecture.LectureRepository;
import com.attendance.modules.lecture.form.LectureForm;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
@RequiredArgsConstructor
public class StudentFormValidator implements Validator {
    private final AccountRepository accountRepository;

    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        StudentForm studentForm = (StudentForm) target;

        if(! accountRepository.existsByNickname(studentForm.getUsername())){
            errors.rejectValue("username","invalid.username",new Object[]{studentForm.getUsername()}, "존재하지 않는 사용자 입니다.");
        }

    }
}