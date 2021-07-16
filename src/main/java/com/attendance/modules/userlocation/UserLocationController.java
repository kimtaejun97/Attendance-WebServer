package com.attendance.modules.userlocation;

import com.attendance.modules.account.Account;
import com.attendance.modules.account.CurrentUser;
import com.attendance.modules.userlocation.form.UserForm;
import com.attendance.modules.userlocation.form.UserFormValidator;
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
public class UserLocationController {

    private final UserLocationService userLocationService;
    private final UserFormValidator userFormValidator;

    @InitBinder("userForm")
    public void initBinder(WebDataBinder webDataBinder){
        webDataBinder.addValidators(userFormValidator);
    }

    @GetMapping("/add-user/{location}")
    public String addUser(@PathVariable String location, Model model){
        UserForm userForm = new UserForm();
        userForm.setLocation(location);

        model.addAttribute(userForm);
        return "user/add-user";
    }

    @PostMapping("place/add-user/{location}")
    public String addStudentForm(@Valid UserForm userForm, Errors errors, @PathVariable String location){
        if(errors.hasErrors()){
            return "user/add-user";
        }
        userLocationService.addUser(userForm.getUsername(),location);

        return "redirect:/user/place/"+location;
    }

    @GetMapping("/public-place/enrollement/{location}")
    public String enrollmentPublicPlace(@PathVariable String location, @CurrentUser Account account){
        userLocationService.connectPlace(account.getUsername(), location);

        return "redirect:/my-place";
    }
}
