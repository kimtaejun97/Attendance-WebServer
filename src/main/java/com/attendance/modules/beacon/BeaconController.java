package com.attendance.modules.beacon;

import com.attendance.modules.account.Account;
import com.attendance.modules.account.AccountRepository;
import com.attendance.modules.account.CurrentUser;
import com.attendance.modules.beacon.form.BeaconForm;
import com.attendance.modules.beacon.validator.BeaconFormValidator;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.util.Set;
import java.util.UUID;

@RequiredArgsConstructor
@Controller
public class BeaconController {

    private final BeaconFormValidator beaconFormValidator;

    private final BeaconRepository beaconRepository;
    private final ModelMapper modelMapper;

    private final BeaconService beaconService;
    private final AccountRepository accountRepository;

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
        beaconService.addBeacon(beaconForm);

        return "redirect:/";
    }

    @GetMapping("/beacon/my-beacon")
    public String myBeacon(@CurrentUser Account account, Model model){
        Set<Beacon> beacons =  beaconService.getBeaconByAccount(account.getUsername());

        model.addAttribute("beacons", beacons);

        return "user/my-beacon";

    }

    @GetMapping("/beacon/remove/{location}")
    public String removeBeacon(@CurrentUser Account account, @PathVariable String location){
        Beacon beacon = beaconRepository.findByLocation(location);
        Account byUsername = accountRepository.findByUsername(account.getUsername());

        if(!beacon.getCreator().equals(account.getUsername())){
            return "redirect:/error";
        }

        byUsername.getBeacons().remove(beacon);
        beaconRepository.delete(beacon);
        return "redirect:/beacon/my-beacon";
    }



}
