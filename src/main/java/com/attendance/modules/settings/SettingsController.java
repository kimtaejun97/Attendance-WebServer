package com.attendance.modules.settings;

import com.attendance.modules.account.Account;
import com.attendance.modules.account.AccountRepository;
import com.attendance.modules.account.CurrentUser;
import com.attendance.modules.settings.form.PasswordForm;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@RequiredArgsConstructor
@Controller
public class SettingsController {
    private final AccountRepository accountRepository;
    private final SettingsService settingsService;

    private final ModelMapper modelMapper;


    @GetMapping ("/settings/password")
    public String accountSettings(Model model){
        model.addAttribute(new PasswordForm());
        return "/settings/account";
    }


    @PostMapping("/settings/password")
    public String changePassword(@CurrentUser Account account, @Valid PasswordForm passwordForm, Errors errors, RedirectAttributes attributes){
        if(errors.hasErrors()){

            return "settings/account";
        }
        settingsService.changePassword(account.getUsername(), passwordForm);



    }
}
