package com.attendance.modules.user;

import com.attendance.modules.account.Account;
import com.attendance.modules.account.CurrentUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

@RequiredArgsConstructor
@Controller
public class UsersController {

    private final UsersService usersService;

    private final UsersFormValidator usersFormValidator;



    @InitBinder("usersForm")
    public void initBinder(WebDataBinder webDataBinder){
        webDataBinder.addValidators(usersFormValidator);
    }

    @GetMapping("/add-user/{location}")
    public String addUser(@PathVariable String location, Model model){
        UsersForm usersForm = new UsersForm();
        usersForm.setLocation(location);

        model.addAttribute(usersForm);
        return "user/add-user";
    }

    @PostMapping("/add-user/{location}")
    public String addStudentForm(@Valid UsersForm usersForm, Errors errors, @PathVariable String location){
        if(errors.hasErrors()){
            return "user/add-user";
        }
        usersService.addUser(usersForm.getUsername(),location);

        return "redirect:/user/place/"+location;
    }

    @GetMapping("/enrollment/public-place/{location}")
    public String enrollmentPublicPlace(@PathVariable String location, @CurrentUser Account account){
        usersService.addUser(account.getNickname(), location);

        return "redirect:/my-place";
    }

}
