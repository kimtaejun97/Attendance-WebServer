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
    private final  AccountRepository accountRepository;
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

        Account account = accountService.createNewAccount(signUpForm);
        accountService.login(account);

        return "redirect:/";

    }

    @GetMapping("/check-email-token")
    public String checkEmailToken(String token, String email, Model model){
        Account account = accountRepository.findByEmail(email);
        String view = "account/checked-email";

        if(account == null){
            model.addAttribute("error","wrong.Email");
            return view;
        }

        if(! account.isValidToken(token)){
            model.addAttribute("error","wrong.Token");
        }
        accountService.completeSignUp(account);
        return view;
    }

    @GetMapping("/check-email")
    public String checkEmail(@CurrentUser Account account, Model model){
        model.addAttribute("email", account.getEmail());

        return "account/check-email";
    }

    @GetMapping("/resend-check-email")
    public String resendCheckEmail(@CurrentUser Account account, Model model){
        model.addAttribute("email", account.getEmail());
        if(!account.canSendEmail()){
            model.addAttribute("error","아직 인증 메일을 재전송 할 수 없습니다. 잠시후에 다시 시도하세요.");
            return "account/check-email";
        }

        accountService.resendCheckEmail(account.getUsername());
        model.addAttribute("success","인증 메일을 재전송 했습니다. 메일함을 확인해주세요.");
        return "account/check-email";
    }

    // TODO 나의 프로필 : 더 채울지
    @GetMapping("/account/my-profile")
    public String myProfile(@CurrentUser Account account, Model model){
        Account newAccount = accountRepository.findByUsername(account.getUsername());


        model.addAttribute(newAccount);
        return "account/my-profile";

    }





}
