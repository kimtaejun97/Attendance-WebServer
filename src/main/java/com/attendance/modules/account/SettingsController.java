package com.attendance.modules.account;

import com.attendance.modules.account.form.PasswordForm;
import com.attendance.modules.account.validator.PasswordFormValidator;
import com.attendance.modules.account.form.ProfileForm;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@RequiredArgsConstructor
@Controller
public class SettingsController {
    private final SettingsService settingsService;
    private final ModelMapper modelMapper;
    private final PasswordFormValidator passwordFormValidator;
    private final AccountService accountService;

    @InitBinder("passwordForm")
    public void passwordInitBinder(WebDataBinder webDataBinder){
        webDataBinder.addValidators(passwordFormValidator);
    }


    @GetMapping("/settings/profile")
    public String profileUpdateForm(@CurrentUser Account account, Model model){
        model.addAttribute(account);
        model.addAttribute(modelMapper.map(account, ProfileForm.class));

        return "settings/profile";
    }

    @PostMapping("/settings/profile")
    public String profileUpdate(@CurrentUser Account account,ProfileForm profileForm){
        settingsService.setProfile(account, profileForm);
        return "redirect:/account/my-profile";
    }


    @GetMapping ("/settings/password")
    public String passwordUpdateForm(@CurrentUser Account account, Model model){
        PasswordForm passwordForm = new PasswordForm();
        passwordForm.setUsername(account.getUsername());
        model.addAttribute(passwordForm);
        return "settings/account";
    }


    @PostMapping("/settings/password")
    public String passwordUpdate(@CurrentUser Account account, @Valid PasswordForm passwordForm, Errors errors, RedirectAttributes attributes){
        if(errors.hasErrors()){
            return "settings/account";
        }
        settingsService.changePassword(account, passwordForm);
        attributes.addFlashAttribute("success", "비밀번호가 성공적으로 변경되었습니다.");
        return "redirect:/settings/password";

    }

    @PostMapping("/account/remove")
    public String removeAccount(@CurrentUser Account account, String confirmString, Model model){
        String username = account.getUsername();

        if(!confirmString.equals(username)){
            model.addAttribute(account);
            model.addAttribute("error","일치하지 않습니다.");
            return "account/my-profile";
        }
        accountService.removeAccount(username);

        return "redirect:/logout";
    }

    // TODO 프로필 수정. 정보 변경, 계정(패스워드, 닉네임 변경?)
}


