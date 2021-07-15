package com.attendance.modules.user;

import com.attendance.modules.account.AccountRepository;
import com.attendance.modules.userplace.UserLocationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
@RequiredArgsConstructor
public class UsersFormValidator implements Validator {
    private final AccountRepository accountRepository;

    private final UserLocationRepository userLocationRepository;

    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.isAssignableFrom(UsersForm.class);

    }

    @Override
    public void validate(Object target, Errors errors) {
        UsersForm usersForm = (UsersForm) target;

        if(! accountRepository.existsByNickname(usersForm.getUsername())){
            errors.rejectValue("username","invalid.username",new Object[]{usersForm.getUsername()}, "존재하지 않는 사용자 입니다.");
        }
        if(userLocationRepository.existsByLocationAndUsername(usersForm.getLocation(), usersForm.getUsername())){
            errors.rejectValue("username","invalid.username",new Object[]{usersForm.getUsername()}, "이미 등록된 사용자 입니다.");
        }

    }
}