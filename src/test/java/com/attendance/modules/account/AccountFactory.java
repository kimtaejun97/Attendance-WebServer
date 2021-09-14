package com.attendance.modules.account;

import com.attendance.modules.account.form.SignUpForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AccountFactory {

    @Autowired
    AccountService accountService;

    public Account createNewAccount(String username){
        SignUpForm signUpForm = new SignUpForm();
        signUpForm.setUsername(username);
        signUpForm.setEmail(username+"@email.com");
        signUpForm.setPassword("123123123");
        signUpForm.setAdminCode("Admin1234");

        return accountService.createAccount(signUpForm);
    }

}
