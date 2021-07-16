package com.attendance.modules.account.form;


import com.attendance.modules.account.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
@RequiredArgsConstructor
public class SignUpFormValidator implements Validator {
    private final AccountRepository accountRepository;




    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.isAssignableFrom(SignUpForm.class);
    }

    @Override
    public void validate(Object target, Errors errors) {
        SignUpForm signUpForm = (SignUpForm)target;

        if(accountRepository.existsByEmail(signUpForm.getEmail())){
            errors.rejectValue("email","invalid.email",new Object[]{signUpForm.getEmail()},"이미 가입된 이메일 입니다.");

        }
        if(accountRepository.existsByUsername(signUpForm.getUsername())){
            errors.rejectValue("username","invalid.username",new Object[]{signUpForm.getUsername()},"이미 사용중인 아이디 입니다.");

        }

        if(signUpForm.getAdminCode() !="" && !signUpForm.getAdminCode().equals("Admin1234")){
            errors.rejectValue("adminCode","invalid.adminCode",new Object[]{signUpForm.getAdminCode()},"잘못된 관리자 코드 입니다.");
        }

    }
}
