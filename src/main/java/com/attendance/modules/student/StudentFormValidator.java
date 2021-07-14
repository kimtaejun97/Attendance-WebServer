package com.attendance.modules.student;

import com.attendance.modules.account.AccountRepository;
import com.attendance.modules.studentlecture.StudentLectureRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
@RequiredArgsConstructor
public class StudentFormValidator implements Validator {
    private final AccountRepository accountRepository;

    private final StudentLectureRepository studentLectureRepository;

    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.isAssignableFrom(StudentForm.class);

    }

    @Override
    public void validate(Object target, Errors errors) {
        StudentForm studentForm = (StudentForm) target;

        if(! accountRepository.existsByNickname(studentForm.getUsername())){
            errors.rejectValue("username","invalid.username",new Object[]{studentForm.getUsername()}, "존재하지 않는 사용자 입니다.");
        }
        if(studentLectureRepository.existsByLectureCodeAndStudentName(studentForm.getLectureCode(),studentForm.getUsername())){
            errors.rejectValue("username","invalid.username",new Object[]{studentForm.getUsername()}, "이미 등록된 사용자 입니다.");
        }

    }
}