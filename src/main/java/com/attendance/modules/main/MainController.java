package com.attendance.modules.main;

import com.attendance.modules.account.Account;
import com.attendance.modules.account.CurrentUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@RequiredArgsConstructor
@Controller
public class MainController {

    @GetMapping("/")
    public String index(@CurrentUser Account account, Model model){
        addAccountIfNotNull(account, model);
        return "index";
    }

    private void addAccountIfNotNull(Account account, Model model) {
        if(account != null){
            model.addAttribute(account);
        }
    }

    @GetMapping("/login")
    public String login(){
        return "login";
    }

}

