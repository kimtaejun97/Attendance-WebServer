package com.attendance.modules.userplace;

import com.attendance.modules.account.Account;
import com.attendance.modules.account.CurrentUser;
import com.attendance.modules.userplace.form.UserForm;
import com.attendance.modules.userplace.form.UserFormValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.support.SessionFlashMapManager;

import javax.validation.Valid;

@RequiredArgsConstructor
@Controller
public class UserPlaceController {

    private final UserPlaceService userPlaceService;
    private final UserFormValidator userFormValidator;

    private final UserPlaceRepository userPlaceRepository;

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
        userPlaceService.connectUserPlace(userForm.getUsername(),location);

        return "redirect:/user/place/"+location;
    }

    @GetMapping("/public-place/enrollment/{location}")
    public String enrollmentPublicPlace(@PathVariable String location, @CurrentUser Account account, Model model, RedirectAttributes attributes){
        if(userPlaceRepository.existsByLocationAndUsername(location,account.getUsername())) {
            attributes.addFlashAttribute("message", "이미 등록된 장소입니다.");

            return "redirect:/public-place-list";
        }

        userPlaceService.connectUserPlace(account.getUsername(), location);
        return "redirect:/my-place";

    }

    //TODO my-place 탈퇴 기능. /my-place/delete-place/{location}

    //TODO place management : 사용자 제거, /place-management/delete-user/{username}


    //TODO 관리자 기능 : place 제거 /admin/delete-place/{location}


}
