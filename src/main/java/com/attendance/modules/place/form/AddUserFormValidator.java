package com.attendance.modules.place.form;

import com.attendance.modules.account.Account;
import com.attendance.modules.account.AccountRepository;
import com.attendance.modules.place.Place;
import com.attendance.modules.accountplace.AccountPlaceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
@RequiredArgsConstructor
public class AddUserFormValidator implements Validator {
    private final AccountRepository accountRepository;
    private final AccountPlaceRepository accountPlaceRepository;

    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.isAssignableFrom(AddUserForm.class);
    }

    @Override
    public void validate(Object target, Errors errors) {
        AddUserForm addUserForm = (AddUserForm) target;
        Account account = accountRepository.findByUsername(addUserForm.getUsername());
        if(account == null){
            errors.rejectValue("username","invalid.username",new Object[]{addUserForm.getUsername()}, "존재하지 않는 사용자 입니다.");
        }
    }

    public void validateIfEnrolledUser(Place place, String username, Errors errors) {
        Account account = accountRepository.findByUsername(username);
        if(isEnrolledAtPlace(place, account)){
                errors.rejectValue("username","invalid.username",new Object[]{username}, "이미 등록된 사용자 입니다.");
            }
    }

    private boolean isEnrolledAtPlace(Place place, Account account) {
        return account != null && accountPlaceRepository.existsByAccountUsernameAndPlaceId(account.getUsername(), place.getId());
    }
}