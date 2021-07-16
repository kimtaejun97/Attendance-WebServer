package com.attendance.modules.beacon;

import com.attendance.modules.account.Account;
import com.attendance.modules.account.CurrentUser;
import com.attendance.modules.beacon.form.BeaconForm;
import com.attendance.modules.beacon.form.BeaconFormValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.util.UUID;

@RequiredArgsConstructor
@Controller
public class BeaconController {

    private final BeaconFormValidator beaconFormValidator;

    private final BeaconRepository beaconRepository;

    @InitBinder("beaconForm")
    public void initBinder(WebDataBinder webDataBinder){
        webDataBinder.addValidators(beaconFormValidator);
    }


    @GetMapping("/add-beacon")
    public String BeaconForm(Model model, @CurrentUser Account account){
        BeaconForm beaconForm = new BeaconForm();
        //TODO 나중에 비콘 수신하는걸로 변경.
        beaconForm.setBeaconCode(UUID.randomUUID().toString());
        beaconForm.setCreator(account.getUsername());

        model.addAttribute(beaconForm);
        return "user/add-beacon";
    }

    @PostMapping("/add-beacon")
    public String addBeacon(@Valid BeaconForm beaconForm, Errors errors){
        if(errors.hasErrors()){
            return "user/add-beacon";
        }

        beaconRepository.save(beaconForm.toEntity());

        return "redirect:/";
    }

}
