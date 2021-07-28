package com.attendance.modules.settings.form;

import com.attendance.modules.account.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@RequiredArgsConstructor
@Component
public class PasswordFormValidator implements Validator {

    private AccountRepository accountRepository;


    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.isAssignableFrom(PasswordForm.class);
    }

    @Override
    public void validate(Object target, Errors errors) {
        PasswordForm passwordForm = (PasswordForm) target;
        accountRepository.findByUsername()

        if()



    }
}
