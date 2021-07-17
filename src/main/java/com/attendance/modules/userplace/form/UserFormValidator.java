package com.attendance.modules.userplace.form;

import com.attendance.modules.account.AccountRepository;
import com.attendance.modules.userplace.UserPlaceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
@RequiredArgsConstructor
public class UserFormValidator implements Validator {
    private final AccountRepository accountRepository;

    private final UserPlaceRepository userPlaceRepository;

    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.isAssignableFrom(UserForm.class);

    }

    @Override
    public void validate(Object target, Errors errors) {
        UserForm userForm = (UserForm) target;

        if(! accountRepository.existsByUsername(userForm.getUsername())){
            errors.rejectValue("username","invalid.username",new Object[]{userForm.getUsername()}, "존재하지 않는 사용자 입니다.");
        }
        if(userPlaceRepository.existsByLocationAndUsername(userForm.getLocation(), userForm.getUsername())){
            errors.rejectValue("username","invalid.username",new Object[]{userForm.getUsername()}, "이미 등록된 사용자 입니다.");
        }

    }
}