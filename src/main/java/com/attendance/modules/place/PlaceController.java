package com.attendance.modules.place;

import com.attendance.modules.account.Account;
import com.attendance.modules.account.CurrentUser;
import com.attendance.modules.place.form.PlaceForm;
import com.attendance.modules.place.form.PlaceFormValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

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

        List<String> users = placeService.getUsersFromPlace(location);

        model.addAttribute(place);
        model.addAttribute("users",users);

        return "admin/place";
    }


    @GetMapping("/create-place")
    public String CreatePlaceForm(@CurrentUser Account account,Model model){
        PlaceForm placeForm = new PlaceForm();
        placeForm.setCreator(account.getNickname());
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
        List<Place> places = placeRepository.findByCreator(account.getNickname());

        model.addAttribute("places", places);
        return "user/my-place";

    }

    @GetMapping("/user/place/{locataion}")
    public String userPlaceInfo(@PathVariable String location, @CurrentUser Account account, Model model){
        boolean isConstructor = placeService.isCreator(location, account.getNickname());

        model.addAttribute("place", placeService.getPlace(location));
        if(isConstructor){
            model.addAttribute("isConstructor",isConstructor);
        }
        return "user/place";
    }

    @GetMapping("/public-place-list")
    public String publicPlaceList(Model model){
        List<Place> places = placeService.getPublicPlaceList();
        model.addAttribute("places",places);

        return "user/public-place-list";
    }


}
