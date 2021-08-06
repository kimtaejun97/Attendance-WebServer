package com.attendance.modules.accountplace.form;

import com.attendance.modules.account.Account;
import com.attendance.modules.account.AccountRepository;
import com.attendance.modules.place.Place;
import com.attendance.modules.place.PlaceRepository;
import com.attendance.modules.accountplace.AccountPlaceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
@RequiredArgsConstructor
public class UserFormValidator implements Validator {
    private final AccountRepository accountRepository;

    private final AccountPlaceRepository accountPlaceRepository;
    private final PlaceRepository placeRepository;

    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.isAssignableFrom(UserForm.class);

    }

    @Override
    public void validate(Object target, Errors errors) {
        UserForm userForm = (UserForm) target;
        Account account = accountRepository.findByUsername(userForm.getUsername());

        if(account == null){
            errors.rejectValue("username","invalid.username",new Object[]{userForm.getUsername()}, "존재하지 않는 사용자 입니다.");
        }



    }

    public void userFormValidation(Place place, String username, Errors errors) {
        Account account = accountRepository.findByUsername(username);
        if(account!=null && accountPlaceRepository.existsByAccountUsernameAndPlaceId(account.getUsername(), place.getId())){
                errors.rejectValue("username","invalid.username",new Object[]{username}, "이미 등록된 사용자 입니다.");
            }

    }
}