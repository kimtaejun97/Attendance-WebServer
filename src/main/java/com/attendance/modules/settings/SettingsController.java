package com.attendance.modules.settings;

import com.attendance.modules.account.Account;
import com.attendance.modules.account.AccountRepository;
import com.attendance.modules.account.AccountService;
import com.attendance.modules.account.CurrentUser;
import com.attendance.modules.settings.form.PasswordForm;
import com.attendance.modules.settings.form.ProfileForm;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.parameters.P;
import org.springframework.security.crypto.password.PasswordEncoder;
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

    private final AccountService accountService;


    @GetMapping("/settings/profile")
    public String profileSettingForm(@CurrentUser Account account, Model model){
        model.addAttribute(account);
        model.addAttribute(modelMapper.map(account, ProfileForm.class));

        return "settings/profile";
    }

    @PostMapping("/settings/profile")
    public String profileSetting(@CurrentUser Account account,ProfileForm profileForm , Model model){
        settingsService.profileSetting(account.getUsername(), profileForm);
        model.addAttribute(account);
        return "redirect:/account/my-profile";
    }


    @GetMapping ("/settings/password")
    public String accountSetting(@CurrentUser Account account, Model model){
        model.addAttribute(new PasswordForm());
        model.addAttribute(account);
        return "settings/account";
    }


    @PostMapping("/settings/password")
    public String changePassword(@CurrentUser Account account, @Valid PasswordForm passwordForm, Errors errors, RedirectAttributes attributes){

        if(errors.hasErrors()){
            return "settings/account";
        }

        settingsService.changePassword(account.getUsername(), passwordForm);
        attributes.addFlashAttribute("success", "비밀번호가 성공적으로 변경되었습니다.");
        return "redirect:/settings/account";

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
