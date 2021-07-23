package com.attendance.modules.userplace;

import com.attendance.modules.account.Account;
import com.attendance.modules.account.AccountRepository;
import com.attendance.modules.account.CurrentUser;
import com.attendance.modules.place.Place;
import com.attendance.modules.place.PlaceRepository;
import com.attendance.modules.userplace.form.UserForm;
import com.attendance.modules.userplace.form.UserFormValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
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
import java.util.Set;

@RequiredArgsConstructor
@Controller
public class UserPlaceController {

    private final UserPlaceService userPlaceService;
    private final UserFormValidator userFormValidator;

    private final PlaceRepository placeRepository;

    private final UserPlaceRepository userPlaceRepository;

    private final AccountRepository accountRepository;

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
        Place place = placeRepository.findByLocation(location);
        account = accountRepository.findById(account.getId()).get();


        if(userPlaceRepository.existsByAccountIdAndPlaceLocation(account.getId(), location)) {
            attributes.addFlashAttribute("message", "이미 등록된 장소입니다.");

            return "redirect:/public-place-list";
        }

        userPlaceService.connectUserPlace(account.getUsername(), location);
        return "redirect:/my-place";

    }

    @GetMapping("/account-place/disconnect-place/{location}")
    public String disConnectPlace(@CurrentUser Account account,@PathVariable String location){

        UserPlace userPlace = userPlaceRepository.findByAccountIdAndPlaceLocation(account.getId(), location);
        userPlaceRepository.delete(userPlace);

        return "redirect:/my-place";
    }

    @GetMapping("/account-place/remove-user/{username}/{location}")
    public String removeUser(@PathVariable String username, @PathVariable String location){
        Account account = accountRepository.findByUsername(username);

        UserPlace userPlace = userPlaceRepository.findByAccountIdAndPlaceLocation(account.getId(), location);
        userPlaceRepository.delete(userPlace);

        return "redirect:/place/management/"+location;
    }



    //TODO place management : 사용자 제거, /place-management/delete-user/{username}




}
