package com.attendance.modules.student;

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
public class UserController {

    private final UserService userService;

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

    @PostMapping("/add-user/{location}")
    public String addStudentForm(@Valid UserForm userForm, Errors errors, @PathVariable String location){
        if(errors.hasErrors()){
            return "user/add-user";
        }
        userService.addUser(userForm,location);

        return "redirect:/user/place/"+location;
    }

}
