package com.attendance.modules.accountplace;

import com.attendance.modules.account.Account;
import com.attendance.modules.account.AccountRepository;
import com.attendance.modules.account.AccountService;
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
    private final PlaceService placeService;
    private final AccountService accountService;

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
        userFormValidator.validateIfEnrolledUser(place,userForm.getUsername(), errors);
        if(errors.hasErrors()){
            model.addAttribute("location", place.getBeacon().getLocation());
            return "user/add-user";
        }
        Account account = accountService.findByUsername(userForm.getUsername());
        accountPlaceService.connectAccountPlace(account,place);

        return "redirect:/place/management/"+place.getEncodedLocation();
    }

    @GetMapping("/public-place/enrollment/{location}")
    public String enrollmentPublicPlace(@PathVariable String location, @CurrentUser Account account, RedirectAttributes attributes){
        Place place = placeService.findByLocation(location);
        account = accountService.findByUsername(account.getUsername());

        if(accountPlaceService.isEnrolled(account,place)) {
            attributes.addFlashAttribute("message", "이미 등록된 장소입니다.");
            return "redirect:/public-place-list";
        }
        accountPlaceService.connectAccountPlace(account, place);

        return "redirect:/my-place";
    }

    @GetMapping("/place/leave/{location}")
    public String disConnectPlace(@CurrentUser Account account,@PathVariable String location){
        accountPlaceService.leave(account, location);
        return "redirect:/my-place";
    }

    @GetMapping("/place/remove-user/{targetUsername}/{location}")
    public String removeUser(@CurrentUser Account account,@PathVariable String targetUsername, @PathVariable String location) throws IllegalAccessException {
        Place place = placeService.findByLocation(location);
        checkAuthority(account, place);
        accountPlaceService.removeUser(targetUsername, place);

        return "redirect:/place/management/"+location;
    }

    private void checkAuthority(Account account, Place place) throws IllegalAccessException {
        if(!requesterIsEqualsToCreator(account, place)){
            throw new IllegalAccessException("접근 권한이 없습니다.");
        }
    }

    private boolean requesterIsEqualsToCreator(Account account, Place place) {
        return placeService.isCreator(place.getLocation(), account);

    }


}
