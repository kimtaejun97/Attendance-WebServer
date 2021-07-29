package com.attendance.modules.settings.form;

import com.attendance.modules.account.Account;
import com.attendance.modules.account.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@RequiredArgsConstructor
@Component
public class PasswordFormValidator implements Validator {

    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;


    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.isAssignableFrom(PasswordForm.class);
    }

    @Override
    public void validate(Object target, Errors errors) {
        PasswordForm passwordForm = (PasswordForm) target;
        Account account = accountRepository.findByUsername(passwordForm.getUsername());

        if(!passwordEncoder.matches(passwordForm.getCurrentPassword(), account.getPassword())){
            errors.rejectValue("currentPassword","wrong.currentPassword","현재 비밀번호와 일치하지 않습니다.");
        }
        if(!passwordForm.getNewPassword().equals(passwordForm.getConfirmPassword())){
            errors.rejectValue("confirmPassword","wrong.confirmPassword","새로운 비밀번호와 확인 비밀번호가 일치하지 않습니다.");

        }
        if(passwordEncoder.matches(passwordForm.getNewPassword(), account.getPassword())){
            errors.rejectValue("newPassword","wrong.newPassword","현재와 동일한 비밀번호로 변경할 수 없습니다.");

        }



    }
}
