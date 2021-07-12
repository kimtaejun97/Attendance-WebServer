package com.attendance.modules.account;


import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@RequiredArgsConstructor
public class SignUpFormValidator implements Validator {
    private final AccountRepository accountRepository;

    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.isAssignableFrom(SignUpForm.class);
    }

    @Override
    public void validate(Object target, Errors errors) {
        var signUpForm = (SignUpForm)target;

        if(accountRepository.existsByEmail(signUpForm.getEmail())){
            errors.rejectValue("email","invalid.email","이미 가입된 이메일 입니다.");
        }

        if(!signUpForm.getAdminCode().equals("jnuAdmin1234")){
            errors.rejectValue("adminCode","invalid.adminCode","잘못된 관리자 코드 입니다.");
        }

    }
}
