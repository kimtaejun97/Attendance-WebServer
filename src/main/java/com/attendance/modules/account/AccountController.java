package com.attendance.modules.account;

import com.attendance.modules.account.form.SignUpForm;
import com.attendance.modules.account.validator.SignUpFormValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

@Controller
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;
    private final SignUpFormValidator signUpFormValidator;

    @InitBinder("signUpForm")
    public void initBinder(WebDataBinder webDataBinder){
        webDataBinder.addValidators(signUpFormValidator);
    }

    @GetMapping("/sign-up")
    public String signUp(Model model){
        model.addAttribute(new SignUpForm());
        return "account/sign-up";
    }

    @PostMapping("/sign-up")
    public String signUpSubmit(@Valid SignUpForm signUpForm, Errors errors){
        if(errors.hasErrors()){
            return "account/sign-up";
        }
        Account account = accountService.createAccount(signUpForm);
        accountService.login(account);

        return "redirect:/";
    }

    @GetMapping("/email-authentication-token")
    public String validEmailAuthenticationToken(String token, String email, Model model){
        Account account = accountService.findByEmail(email);
        accountService.validateAuthentication(account,token);
        accountService.completeSignUp(account);

        return "account/checked-email";
    }

    @GetMapping("/check-email")
    public String checkEmail(@CurrentUser Account account, Model model){
        model.addAttribute("email", account.getEmail());

        return "account/check-email";
    }

    @GetMapping("/resend-authentication-email")
    public String resendCheckEmail(@CurrentUser Account account, Model model){
        model.addAttribute("email", account.getEmail());
        accountService.resendAuthenticationEmail(account.getUsername(), model);

        return "account/check-email";
    }

    // TODO 나의 프로필 : 더 채울지
    @GetMapping("/account")
    public String myProfile(@CurrentUser Account account, Model model){
        Account persistenceAccount = accountService.findByUsername(account.getUsername());
        model.addAttribute(persistenceAccount);

        return "account/my-profile";
    }





}
