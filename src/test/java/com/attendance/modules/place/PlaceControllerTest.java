package com.attendance.modules.place;

import com.attendance.WithAccount;
import com.attendance.modules.account.*;
import com.attendance.modules.accountplace.AccountPlace;
import com.attendance.modules.beacon.BeaconFactory;
import com.attendance.modules.beacon.BeaconRepository;
import com.attendance.modules.accountplace.AccountPlaceRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
class PlaceControllerTest {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    PlaceService placeService;
    @Autowired
    PlaceRepository placeRepository;
    @Autowired
    AccountService accountService;
    @Autowired
    AccountRepository accountRepository;
    @Autowired
    BeaconRepository beaconRepository;
    @Autowired
    AccountPlaceRepository accountPlaceRepository;
    @Autowired
    PlaceFactory placeFactory;
    @Autowired
    BeaconFactory beaconFactory;
    @Autowired
    AccountFactory accountFactory;


    @AfterEach
    void cleanup(){
        beaconRepository.deleteAll();
        placeRepository.deleteAll();
        accountPlaceRepository.deleteAll();
        accountRepository.deleteAll();
    }


    @WithAccount(Value = "bigave")
    @DisplayName("admin ?????????")
    @Test
    void adminPage() throws Exception {

        mockMvc.perform(get("/admin-page"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/admin-page"))
                .andExpect(model().attributeExists("places"));
    }

    @WithAccount(Value = "bigave")
    @DisplayName("?????? ?????? View")
    @Test
    void createPlaceView() throws Exception {

        mockMvc.perform(get("/create-place"))
                .andExpect(status().isOk())
                .andExpect(view().name("user/create-place"))
                .andExpect(model().attributeExists("placeForm"));
    }

    @WithAccount(Value = "bigave")
    @DisplayName("?????? ?????? - ????????? ??????")
    @Test
    void createPlace_with_correct_input() throws Exception {
        Account account = accountRepository.findByUsername("bigave");
        beaconFactory.createNewBeacon("??????", account,null);


        mockMvc.perform(post("/create-place")
        .param("location", "??????")
        .param("alias", "??? ??????")
        .param("creatorName", "bigave")
        .param("isPublic", "on")
        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/my-place"));

        assertTrue(placeRepository.existsByLocation("??????"));
    }

    @WithAccount(Value = "bigave")
    @DisplayName("?????? ?????? - ????????? ?????? : ???????????? ?????? ????????????")
    @Test
    void createPlace_with_wrong_input() throws Exception {

        mockMvc.perform(post("/create-place")
                .param("location", "nonono")
                .param("alias", "??? ??????")
                .param("creatorName", "bigave")
                .param("isPublic", "on")
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("user/create-place"));
    }

    @WithAccount(Value = "bigave")
    @DisplayName("?????? ?????? - ?????? ???????????? ?????? ??????")
    @Test
    void createPlace_with_exists_input() throws Exception {
        Account account = accountRepository.findByUsername("bigave");
        placeFactory.createNewPlace("??????", account);

        mockMvc.perform(post("/create-place")
                .param("location", "??????")
                .param("alias", "??? ??????")
                .param("creatorName", "bigave")
                .param("isPublic", "on")
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(model().hasErrors())
                .andExpect(view().name("user/create-place"));
    }

    @WithAccount(Value = "other")
    @DisplayName("?????? ?????? - ?????? ??? ??? ?????? ??????")
    @Test
    void createPlace_with_invalid_creator() throws Exception {
        Account account = accountFactory.createNewAccount("bigave");
        beaconFactory.createNewBeacon("??????", account,"aaaa-bbbb-cccc");

        Account creator = accountRepository.findByUsername("other");

        mockMvc.perform(post("/create-place")
                .param("location", "??????")
                .param("alias", "??? ??????")
                .param("creatorName", "other")
                .param("isPublic", "on")
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(model().hasErrors())
                .andExpect(view().name("user/create-place"));
    }

    @WithAccount(Value = "bigave")
    @DisplayName("admin - ?????? ?????? View")
    @Test
    void adminPlaceInfo() throws Exception {
        Account account = accountRepository.findByUsername("bigave");
        placeFactory.createNewPlace("??????", account);

        mockMvc.perform(get("/admin/place-users/??????"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/place"))
                .andExpect(model().attributeExists("place"))
                .andExpect(model().attributeExists("users"));
    }


    @WithAccount(Value = "bigave")
    @DisplayName("?????? ????????? ?????? ?????????")
    @Test
    void myPlace() throws Exception {

        mockMvc.perform(get("/my-place"))
                .andExpect(status().isOk())
                .andExpect(view().name("user/my-place"))
                .andExpect(model().attributeExists("places"));
    }


    @WithAccount(Value = "bigave")
    @Test
    @DisplayName("?????? ??????")
    void removePlace() throws Exception {
        Account account = accountRepository.findByUsername("bigave");
        placeFactory.createNewPlace("??????", account);

        Place place = placeRepository.findByLocationFromBeacon("??????");
        assertNotNull(place);

        mockMvc.perform(post("/place/remove/??????"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/my-place"));

        assertFalse(placeRepository.existsByLocation("??????"));
    }

    @WithAccount(Value = "bigave")
    @Test
    @DisplayName("?????? ????????? ?????? ?????? ?????????")
    void myPlaceManagement() throws Exception {
        Account account = accountRepository.findByUsername("bigave");
        placeFactory.createNewPlace("??????", account);

        mockMvc.perform(get("/place/management/??????"))
                .andExpect(status().isOk())
                .andExpect(view().name("user/place-management"))
                .andExpect(model().attributeExists("place"))
                .andExpect(model().attributeExists("users"));

    }

    @WithAccount(Value = "kim")
    @Test
    @DisplayName("?????? ????????? ?????? ?????? ????????? - ???????????? ??????.")
    void myPlaceManagement_invalid_user() throws Exception {
        Account account = accountFactory.createNewAccount("bigave");
        placeFactory.createNewPlace("??????", account);

        mockMvc.perform(get("/place/management/??????"))
                .andExpect(status().is4xxClientError());

    }

    @WithAccount(Value = "bigave")
    @DisplayName("????????? ????????? : ?????? ??????")
    @Test
    void admin_remove_place() throws Exception {
        Account account = accountFactory.createNewAccount("user");
        placeFactory.createNewPlace("??????", account);

        mockMvc.perform(post("/place/admin/remove/??????"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin-page"));

        assertFalse(placeRepository.existsByLocation("??????"));
    }

    @Transactional
    @WithAccount(Value = "bigave")
    @DisplayName("????????? ????????? : ????????? ?????? - ?????? ?????? ?????????")
    @Test
    void remove_place_commonUser() throws Exception {
        Account account = accountFactory.createNewAccount("user");
        placeFactory.createNewPlace("??????", account);

        Account byUsername = accountRepository.findByUsername("bigave");
        byUsername.setRole(Role.USER);

        mockMvc.perform(get("/place/admin/remove/??????"))
                .andExpect(status().is4xxClientError());

        assertTrue(placeRepository.existsByLocation("??????"));

    }

    @WithAccount(Value = "bigave")
    @DisplayName("????????? :: ????????? ?????? ??????")
    @Test
    void addUserView() throws Exception {
        Account account = accountRepository.findByUsername("bigave");
        placeFactory.createNewPlace("??????", account);

        mockMvc.perform(get("/user/??????"))
                .andExpect(status().isOk())
                .andExpect(view().name("user/add-user"));
    }

    @WithAccount(Value = "bigave")
    @DisplayName("????????? :: ????????? ?????? - ?????? ??????")
    @Test
    void addUser_with_correct_input() throws Exception {
        Account account = accountRepository.findByUsername("bigave");
        placeFactory.createNewPlace("??????", account);
        String encodedLocation = URLEncoder.encode("??????", StandardCharsets.UTF_8);

        Account user = accountFactory.createNewAccount("user");


        mockMvc.perform(post("/user")
                .param("username","user")
                .param("location","??????")
                .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/place/management/"+encodedLocation))
                .andExpect(model().attributeDoesNotExist("errors"));

        Place place = placeRepository.findByLocationFromBeacon("??????");

        boolean isRegistered = accountPlaceRepository.existsByAccountAndPlace(user,place);

        assertTrue(isRegistered);


    }
    @WithAccount(Value = "bigave")
    @DisplayName("????????? :: ????????? ?????? - ???????????? ?????? ?????????")
    @Test
    void addUser_with_nonexist_user() throws Exception {
        Account account = accountRepository.findByUsername("bigave");
        placeFactory.createNewPlace("??????", account);

        mockMvc.perform(post("/user")
                .param("location", "??????")
                .param("username", "none")
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("user/add-user"))
                .andExpect(model().hasErrors());
    }

    @WithAccount(Value = "bigave")
    @DisplayName("????????? :: ????????? ?????? - ?????? ????????? ?????????")
    @Test
    void addUser_duplicated_user() throws Exception {
        Account account = accountRepository.findByUsername("bigave");
        Place place = placeFactory.createNewPlace("??????",account);

        accountPlaceRepository.save(AccountPlace.builder()
                .account(account)
                .place(place)
                .build());

        mockMvc.perform(post("/user")
                .param("username","bigave")
                .param("location","??????")
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("user/add-user"))
                .andExpect(model().hasErrors());

    }

    @WithAccount(Value = "kim")
    @DisplayName("????????? ?????? :: ?????? ??????")
    @Test
    void publicPlace_join() throws Exception {
        Account account = accountRepository.findByUsername("bigave");
        placeFactory.createNewPlace("??????", account);

        mockMvc.perform(post("/place/join/??????")
                .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/my-place"));
    }
    @WithAccount(Value = "bigave")
    @DisplayName("???????????? ??????")
    @Test
    void disconnectPlace() throws Exception {
        Account account = accountRepository.findByUsername("bigave");
        Place place = placeFactory.createNewPlace("??????", account);
        placeService.connectAccountPlace(account,place);

        mockMvc.perform(post("/place/leave/??????"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/my-place"));

        assertFalse(accountPlaceRepository.existsByAccountUsernameAndPlaceId(account.getUsername(), place.getId()));
    }



    @WithAccount(Value = "bigave")
    @DisplayName("???????????? ????????? ??????")
    @Test
    void removeUser() throws Exception {
        Account account = accountRepository.findByUsername("bigave");
        Place place = placeFactory.createNewPlace("??????", account);

        Account kim = accountFactory.createNewAccount("kim");
        placeService.connectAccountPlace(kim,place);

        mockMvc.perform(get("/place/remove-user/kim/??????"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/place/management/??????"));

        assertFalse(accountPlaceRepository.existsByAccountUsernameAndPlaceId(kim.getUsername(), place.getId()));

    }
    @WithAccount(Value = "wrong")
    @DisplayName("???????????? ????????? ?????? - ???????????? ?????? ???????????? ??????")
    @Test
    void removeUser_invalid_userRequest() throws Exception {
        Account account = accountFactory.createNewAccount("bigave");
        Place place = placeFactory.createNewPlace("??????", account);

        Account kim = accountFactory.createNewAccount("kim");
        placeService.connectAccountPlace(kim,place);

        mockMvc.perform(get("/user/kim/??????"))
                .andExpect(status().is4xxClientError());

        assertTrue(accountPlaceRepository.existsByAccountUsernameAndPlaceId(kim.getUsername(), place.getId()));

    }




}