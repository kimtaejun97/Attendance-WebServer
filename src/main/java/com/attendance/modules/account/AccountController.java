package com.attendance.modules.account;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

@Controller
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;
    private final  AccountRepository accountRepository;


    @GetMapping("/sign-up")
    public String signUp(Model model){
        model.addAttribute(new SignUpForm());
        return "account/sign-up";

    }

    @PostMapping("/sign-up")
    public String signUpSubmit(@Valid SignUpForm signUpForm){
        accountService.createNewAccount(signUpForm);

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
}
