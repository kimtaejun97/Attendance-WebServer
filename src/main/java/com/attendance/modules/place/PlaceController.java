package com.attendance.modules.place;

import com.attendance.modules.account.Account;
import com.attendance.modules.account.AccountRepository;
import com.attendance.modules.account.CurrentUser;
import com.attendance.modules.place.form.PlaceForm;
import com.attendance.modules.place.form.PlaceFormValidator;
import com.attendance.modules.userplace.UserPlace;
import com.attendance.modules.userplace.UserPlaceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Set;

@RequiredArgsConstructor
@Controller
public class PlaceController {

    private final PlaceService placeService;
    private final PlaceRepository placeRepository;
    private final PlaceFormValidator placeFormValidator;


    @InitBinder("placeForm")
    public void initBinder(WebDataBinder webDataBinder){
        webDataBinder.addValidators(placeFormValidator);
    }

    @GetMapping("/admin-page")
    public String adminPage(Model model){
        List<PlaceListResponseDto> places = placeService.getPlaceList();
        model.addAttribute("places", places);

        return "admin/admin-page";
    }

    @GetMapping("/admin/place/{location}")
    public String adminPlaceInfo (@PathVariable String location, Model model){

        Place place = placeRepository.findByLocation(location);
        List<String> users = placeService.getUsersFromPlace(place);

        model.addAttribute(place);
        model.addAttribute("users",users);

        return "admin/place";
    }

    @GetMapping("/create-place")
    public String CreatePlaceForm(@CurrentUser Account account,Model model){
        PlaceForm placeForm = new PlaceForm();
        placeForm.setCreator(account.getUsername());
        model.addAttribute(placeForm);

        return "user/create-place";
    }

    @PostMapping("/create-place")
    public String createPlace(@Valid PlaceForm placeForm, Errors errors, String isPublic){

        if(errors.hasErrors()){
            return "user/create-place";
        }
        placeService.createPlace(placeForm, isPublic);

        return "redirect:/";
    }

    @GetMapping("/my-place")
    public String myPlace(@CurrentUser Account account, Model model){

        List<PlaceListResponseDto> places = placeService.getPlacesFromUser(account);

        model.addAttribute("places", places);
        return "user/my-place";

    }

    @GetMapping("/public-place-list")
    public String publicPlaceList(Model model){
        List<PlaceListResponseDto> places = placeService.getPublicPlaceList();
        model.addAttribute("places",places);

        return "user/public-place-list";
    }

    @GetMapping("/place/remove-place/{location}")
    public String removePlace(@PathVariable String location){
        Place place = placeRepository.findByLocation(location);
        placeRepository.delete(place);

        return "redirect:/my-place";
    }


    //TODO 내가 생성한 장소 관리 페이지 /place/management/

    @GetMapping("/place/management/{location}")
    public String placeManagement(@PathVariable String location, Model model){
        Place place = placeRepository.findByLocation(location);

        List<String> users = placeService.getUsersFromPlace(place);

        model.addAttribute(place);
        model.addAttribute("users",users);

        return "user/place-management";
    }





}
