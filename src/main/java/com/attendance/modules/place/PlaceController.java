package com.attendance.modules.place;

import com.attendance.modules.account.Account;
import com.attendance.modules.account.AccountService;
import com.attendance.modules.account.CurrentUser;
import com.attendance.modules.place.form.AddUserForm;
import com.attendance.modules.place.form.AddUserFormValidator;
import com.attendance.modules.place.form.PlaceForm;
import com.attendance.modules.place.validator.PlaceFormValidator;
import com.attendance.modules.place.form.PlaceListResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.List;

@RequiredArgsConstructor
@Controller
public class PlaceController {

    private final PlaceService placeService;
    private final PlaceFormValidator placeFormValidator;
    private final AddUserFormValidator addUserFormValidator;
    private final AccountService accountService;


    @InitBinder("placeForm")
    public void placeFormInitBinder(WebDataBinder webDataBinder){
        webDataBinder.addValidators(placeFormValidator);
    }

    @InitBinder("addUserForm")
    public void userFormInitBinder(WebDataBinder webDataBinder){
        webDataBinder.addValidators(addUserFormValidator);
    }

    @GetMapping("/create-place")
    public String CreatePlaceForm(@CurrentUser Account account,Model model){
        PlaceForm placeForm = new PlaceForm();
        placeForm.setCreatorName(account.getUsername());
        model.addAttribute(placeForm);

        return "user/create-place";
    }

    @PostMapping("/create-place")
    public String createPlace(@CurrentUser Account account, @Valid PlaceForm placeForm, Errors errors){
        placeFormValidator.validateEqualsToBeaconCreator(account,placeForm.getLocation(), errors);
        if(errors.hasErrors()){
            return "user/create-place";
        }
        placeService.createPlace(placeForm, account);

        return "redirect:/my-place";
    }

    @GetMapping("/admin-page")
    public String adminPage(Model model){
        List<PlaceListResponseDto> places = placeService.getPlacesForAdmin();
        model.addAttribute("places", places);

        return "admin/admin-page";
    }

    @GetMapping("/admin/place-users/{location}")
    public String adminPlaceInfo (@PathVariable String location, Model model){
        Place place = placeService.findByLocation(location);
        List<String> users = placeService.getUsersByPlace(place);
        model.addAttribute(place);
        model.addAttribute("users",users);

        return "admin/place";
    }

    @PostMapping("/place/admin/remove/{location}")
    public String removePlaceWithAdmin(@CurrentUser Account account, @PathVariable String location) throws IllegalAccessException {
        account.validateIsAdmin();
        placeService.remove(location);

        return "redirect:/admin-page";
    }

    @GetMapping("/my-place")
    public String myPlace(@CurrentUser Account account, Model model){
        List<PlaceListResponseDto> places = placeService.getPlacesByAccount(account);
        model.addAttribute("places", places);

        return "user/my-place";
    }

    @GetMapping("/public-place-list")
    public String publicPlaceList(Model model){
        List<PlaceListResponseDto> places = placeService.getPublicPlaceList();
        model.addAttribute("places",places);

        return "user/public-place-list";
    }

    @PostMapping("/place/remove/{location}")
    public String removePlace(@PathVariable String location){
        placeService.remove(location);
        return "redirect:/my-place";
    }


    @GetMapping("/place/management/{location}")
    public String placeManagement(@CurrentUser Account account,@PathVariable String location, Model model) throws IllegalAccessException {
        Place place = placeService.findByLocation(location);
        place.validateEqualsToCreator(account);
        List<String> users = placeService.getUsersByPlace(place);

        model.addAttribute(place);
        model.addAttribute("users",users);
        return "user/place-management";
    }

    @GetMapping("/user/{location}")
    public String addUser(@PathVariable String location, Model model){
        model.addAttribute(new AddUserForm(location));
        return "user/add-user";
    }

    @PostMapping("/user")
    public String addStudentForm(@Valid AddUserForm addUserForm, Errors errors){
        Place place = placeService.findByLocation(addUserForm.getLocation());
        addUserFormValidator.validateIfEnrolledUser(place, addUserForm.getUsername(), errors);
        if(errors.hasErrors()){
            return "user/add-user";

        }
        placeService.addUser(addUserForm.getUsername(),place);

        return "redirect:/place/management/"+place.getEncodedLocation();
    }

    @PostMapping("/place/join/{location}")
    public String enrollmentPublicPlace(@PathVariable String location, @CurrentUser Account account, RedirectAttributes attributes){
        Place place = placeService.findByLocation(location);
        account = accountService.findByUsername(account.getUsername());
        if(placeService.isEnrolled(account,place)) {
            attributes.addFlashAttribute("message", "이미 등록된 장소입니다.");
            return "redirect:/public-place-list";
        }
        placeService.connectAccountPlace(account, place);

        return "redirect:/my-place";
    }

    @PostMapping("/place/leave/{location}")
    public String disConnectPlace(@CurrentUser Account account,@PathVariable String location){
        placeService.leave(account, location);
        return "redirect:/my-place";
    }

    @GetMapping("/place/remove-user/{targetUsername}/{location}")
    public String removeUser(@CurrentUser Account account,@PathVariable String targetUsername, @PathVariable String location) throws IllegalAccessException {
        Place place = placeService.findByLocation(location);
        checkAuthority(account, place);
        placeService.removeUser(targetUsername, place);

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
