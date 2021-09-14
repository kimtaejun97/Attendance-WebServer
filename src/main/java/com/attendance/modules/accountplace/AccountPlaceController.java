package com.attendance.modules.accountplace;

import com.attendance.modules.account.Account;
import com.attendance.modules.account.AccountRepository;
import com.attendance.modules.account.CurrentUser;
import com.attendance.modules.place.Place;
import com.attendance.modules.place.PlaceRepository;
import com.attendance.modules.accountplace.form.UserForm;
import com.attendance.modules.accountplace.form.UserFormValidator;
import com.attendance.modules.place.PlaceService;
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

import javax.validation.Valid;

@RequiredArgsConstructor
@Controller
public class AccountPlaceController {

    private final AccountPlaceService accountPlaceService;
    private final UserFormValidator userFormValidator;
    private final PlaceRepository placeRepository;
    private final PlaceService placeService;
    private final AccountPlaceRepository accountPlaceRepository;
    private final AccountRepository accountRepository;

    @InitBinder("userForm")
    public void initBinder(WebDataBinder webDataBinder){
        webDataBinder.addValidators(userFormValidator);
    }

    @GetMapping("place/add-user/{location}")
    public String addUser(@PathVariable String location, Model model){
        model.addAttribute("location", location);
        model.addAttribute(new UserForm());

        return "user/add-user";
    }

    @PostMapping("/place/add-user/{location}")
    public String addStudentForm(@Valid UserForm userForm, Errors errors, @PathVariable String location, Model model){
        Place place = placeService.findByLocation(location);

        userFormValidator.userFormValidation(place,userForm.getUsername(), errors);

        if(errors.hasErrors()){
            model.addAttribute("location", place.getBeacon().getLocation());
            return "user/add-user";
        }
        Account account = accountRepository.findByUsername(userForm.getUsername());
        accountPlaceService.connectAccountPlace(account,place);

        return "redirect:/user/place/"+place.getBeacon().getLocation();
    }

    @GetMapping("/public-place/enrollment/{location}")
    public String enrollmentPublicPlace(@PathVariable String location, @CurrentUser Account account, Model model, RedirectAttributes attributes){
        Place place = placeRepository.findByLocation(location);
        account = accountRepository.findById(account.getUsername()).get();


        if(accountPlaceRepository.existsByAccountUsernameAndPlaceId(account.getUsername(), place.getId())) {
            attributes.addFlashAttribute("message", "이미 등록된 장소입니다.");

            return "redirect:/public-place-list";
        }

        accountPlaceService.connectAccountPlace(account, place);
        return "redirect:/my-place";

    }

    @GetMapping("/account-place/disconnect-place/{location}")
    public String disConnectPlace(@CurrentUser Account account,@PathVariable String location){
        Place place = placeRepository.findByLocation(location);

        accountPlaceService.disconnent(account, place);


        return "redirect:/my-place";
    }

    @GetMapping("/account-place/remove-user/{username}/{location}")
    public String removeUser(@CurrentUser Account account,@PathVariable String username, @PathVariable String location){
        Place place = placeRepository.findByLocation(location);
        if(!place.getCreator().equals(account)){
            return "redirect:/error";
        }

        Account byUsername = accountRepository.findByUsername(username);

        AccountPlace accountPlace = accountPlaceRepository.findByAccountUsernameAndPlaceId(byUsername.getUsername(), place.getId());
        accountPlaceRepository.delete(accountPlace);

        return "redirect:/place/management/"+location;
    }









}
