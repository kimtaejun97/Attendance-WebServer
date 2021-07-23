package com.attendance;

import com.attendance.modules.account.Account;
import com.attendance.modules.account.AccountService;
import com.attendance.modules.account.form.SignUpForm;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.test.context.support.WithSecurityContextFactory;



@RequiredArgsConstructor
public class WithAccountSecurityContextFactory implements WithSecurityContextFactory<WithAccount> {

    private final AccountService accountService;

    @Override
    public SecurityContext createSecurityContext(WithAccount withAccount) {
        SignUpForm signUpForm = new SignUpForm();
        signUpForm.setUsername(withAccount.Value());
        signUpForm.setEmail("test@email.com");
        signUpForm.setPassword("123123123");

        accountService.createNewAccount(signUpForm);

        UserDetails principal = accountService.loadUserByUsername(withAccount.Value());

        Authentication authentication = new UsernamePasswordAuthenticationToken(principal, principal.getPassword(), principal.getAuthorities());
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authentication);

        return context;
    }

}
