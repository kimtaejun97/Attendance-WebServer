package com.attendance.modules.place;

import com.attendance.WithAccount;
import com.attendance.modules.account.*;
import com.attendance.modules.accountplace.AccountPlace;
import com.attendance.modules.beacon.BeaconFactory;
import com.attendance.modules.beacon.BeaconRepository;
import com.attendance.modules.accountplace.AccountPlaceRepository;
import com.attendance.modules.place.form.AddUserForm;
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
    @DisplayName("admin 페이지")
    @Test
    void adminPage() throws Exception {

        mockMvc.perform(get("/admin-page"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/admin-page"))
                .andExpect(model().attributeExists("places"));
    }

    @WithAccount(Value = "bigave")
    @DisplayName("장소 추가 View")
    @Test
    void createPlaceView() throws Exception {

        mockMvc.perform(get("/create-place"))
                .andExpect(status().isOk())
                .andExpect(view().name("user/create-place"))
                .andExpect(model().attributeExists("placeForm"));
    }

    @WithAccount(Value = "bigave")
    @DisplayName("장소 추가 - 입력값 정상")
    @Test
    void createPlace_with_correct_input() throws Exception {
        Account account = accountRepository.findByUsername("bigave");
        beaconFactory.createNewBeacon("광주", account,null);


        mockMvc.perform(post("/create-place")
        .param("location", "광주")
        .param("alias", "내 지역")
        .param("creatorName", "bigave")
        .param("isPublic", "on")
        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/my-place"));

        assertTrue(placeRepository.existsByLocation("광주"));
    }

    @WithAccount(Value = "bigave")
    @DisplayName("장소 추가 - 입력값 오류 : 존재하지 않는 비콘위치")
    @Test
    void createPlace_with_wrong_input() throws Exception {

        mockMvc.perform(post("/create-place")
                .param("location", "nonono")
                .param("alias", "내 지역")
                .param("creatorName", "bigave")
                .param("isPublic", "on")
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("user/create-place"));
    }

    @WithAccount(Value = "bigave")
    @DisplayName("장소 생성 - 이미 생성되어 있는 위치")
    @Test
    void createPlace_with_exists_input() throws Exception {
        Account account = accountRepository.findByUsername("bigave");
        placeFactory.createNewPlace("광주", account);

        mockMvc.perform(post("/create-place")
                .param("location", "광주")
                .param("alias", "내 지역")
                .param("creatorName", "bigave")
                .param("isPublic", "on")
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(model().hasErrors())
                .andExpect(view().name("user/create-place"));
    }

    @WithAccount(Value = "other")
    @DisplayName("장소 생성 - 접근 할 수 없는 비콘")
    @Test
    void createPlace_with_invalid_creator() throws Exception {
        Account account = accountFactory.createNewAccount("bigave");
        beaconFactory.createNewBeacon("광주", account,"aaaa-bbbb-cccc");

        Account creator = accountRepository.findByUsername("other");

        mockMvc.perform(post("/create-place")
                .param("location", "광주")
                .param("alias", "내 지역")
                .param("creatorName", "other")
                .param("isPublic", "on")
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(model().hasErrors())
                .andExpect(view().name("user/create-place"));
    }

    @WithAccount(Value = "bigave")
    @DisplayName("admin - 장소 정보 View")
    @Test
    void adminPlaceInfo() throws Exception {
        Account account = accountRepository.findByUsername("bigave");
        placeFactory.createNewPlace("광주", account);

        mockMvc.perform(get("/admin/place-users/광주"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/place"))
                .andExpect(model().attributeExists("place"))
                .andExpect(model().attributeExists("users"));
    }


    @WithAccount(Value = "bigave")
    @DisplayName("내가 등록된 장소 페이지")
    @Test
    void myPlace() throws Exception {

        mockMvc.perform(get("/my-place"))
                .andExpect(status().isOk())
                .andExpect(view().name("user/my-place"))
                .andExpect(model().attributeExists("places"));
    }


    @WithAccount(Value = "bigave")
    @Test
    @DisplayName("장소 제거")
    void removePlace() throws Exception {
        Account account = accountRepository.findByUsername("bigave");
        placeFactory.createNewPlace("광주", account);

        Place place = placeRepository.findByLocation("광주");
        assertNotNull(place);

        mockMvc.perform(post("/place/remove/광주"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/my-place"));

        assertFalse(placeRepository.existsByLocation("광주"));
    }

    @WithAccount(Value = "bigave")
    @Test
    @DisplayName("내가 생성한 장소 관리 페이지")
    void myPlaceManagement() throws Exception {
        Account account = accountRepository.findByUsername("bigave");
        placeFactory.createNewPlace("광주", account);

        mockMvc.perform(get("/place/management/광주"))
                .andExpect(status().isOk())
                .andExpect(view().name("user/place-management"))
                .andExpect(model().attributeExists("place"))
                .andExpect(model().attributeExists("users"));

    }

    @WithAccount(Value = "kim")
    @Test
    @DisplayName("내가 생성한 장소 관리 페이지 - 생성자가 아님.")
    void myPlaceManagement_invalid_user() throws Exception {
        Account account = accountFactory.createNewAccount("bigave");
        placeFactory.createNewPlace("광주", account);

        mockMvc.perform(get("/place/management/광주"))
                .andExpect(status().is4xxClientError());

    }

    @WithAccount(Value = "bigave")
    @DisplayName("관리자 페이지 : 장소 제거")
    @Test
    void admin_remove_place() throws Exception {
        Account account = accountFactory.createNewAccount("user");
        placeFactory.createNewPlace("광주", account);

        mockMvc.perform(post("/place/admin/remove/광주"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin-page"));

        assertFalse(placeRepository.existsByLocation("광주"));
    }

    @Transactional
    @WithAccount(Value = "bigave")
    @DisplayName("관리자 페이지 : 사용자 제거 - 권한 없는 사용자")
    @Test
    void remove_place_commonUser() throws Exception {
        Account account = accountFactory.createNewAccount("user");
        placeFactory.createNewPlace("광주", account);

        Account byUsername = accountRepository.findByUsername("bigave");
        byUsername.setRole(Role.USER);

        mockMvc.perform(get("/place/admin/remove/광주"))
                .andExpect(status().is4xxClientError());

        assertTrue(placeRepository.existsByLocation("광주"));

    }

    @WithAccount(Value = "bigave")
    @DisplayName("생성자 :: 사용자 추가 화면")
    @Test
    void addUserView() throws Exception {
        Account account = accountRepository.findByUsername("bigave");
        placeFactory.createNewPlace("광주", account);

        mockMvc.perform(get("/user/광주"))
                .andExpect(status().isOk())
                .andExpect(view().name("user/add-user"));
    }

    @WithAccount(Value = "bigave")
    @DisplayName("생성자 :: 사용자 추가 - 정상 입력")
    @Test
    void addUser_with_correct_input() throws Exception {
        Account account = accountRepository.findByUsername("bigave");
        placeFactory.createNewPlace("광주", account);
        String encodedLocation = URLEncoder.encode("광주", StandardCharsets.UTF_8);

        Account user = accountFactory.createNewAccount("user");


        mockMvc.perform(post("/user")
                .param("username","user")
                .param("location","광주")
                .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/place/management/"+encodedLocation))
                .andExpect(model().attributeDoesNotExist("errors"));

        Place place = placeRepository.findByLocation("광주");

        boolean isRegistered = accountPlaceRepository.existsByAccountAndPlace(user,place);

        assertTrue(isRegistered);


    }
    @WithAccount(Value = "bigave")
    @DisplayName("생성자 :: 사용자 추가 - 존재하지 않는 사용자")
    @Test
    void addUser_with_nonexist_user() throws Exception {
        Account account = accountRepository.findByUsername("bigave");
        placeFactory.createNewPlace("광주", account);

        mockMvc.perform(post("/user")
                .param("location", "광주")
                .param("username", "none")
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("user/add-user"))
                .andExpect(model().hasErrors());
    }

    @WithAccount(Value = "bigave")
    @DisplayName("생성자 :: 사용자 추가 - 이미 등록된 사용자")
    @Test
    void addUser_duplicated_user() throws Exception {
        Account account = accountRepository.findByUsername("bigave");
        Place place = placeFactory.createNewPlace("광주",account);

        accountPlaceRepository.save(AccountPlace.builder()
                .account(account)
                .place(place)
                .build());

        mockMvc.perform(post("/user")
                .param("username","bigave")
                .param("location","광주")
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("user/add-user"))
                .andExpect(model().hasErrors());

    }

    @WithAccount(Value = "kim")
    @DisplayName("공개된 장소 :: 나를 추가")
    @Test
    void publicPlace_join() throws Exception {
        Account account = accountRepository.findByUsername("bigave");
        placeFactory.createNewPlace("광주", account);

        mockMvc.perform(post("/place/join/광주")
                .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/my-place"));
    }
    @WithAccount(Value = "bigave")
    @DisplayName("장소에서 탈퇴")
    @Test
    void disconnectPlace() throws Exception {
        Account account = accountRepository.findByUsername("bigave");
        Place place = placeFactory.createNewPlace("광주", account);
        placeService.connectAccountPlace(account,place);

        mockMvc.perform(post("/place/leave/광주"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/my-place"));

        assertFalse(accountPlaceRepository.existsByAccountUsernameAndPlaceId(account.getUsername(), place.getId()));
    }



    @WithAccount(Value = "bigave")
    @DisplayName("장소에서 사용자 제거")
    @Test
    void removeUser() throws Exception {
        Account account = accountRepository.findByUsername("bigave");
        Place place = placeFactory.createNewPlace("광주", account);

        Account kim = accountFactory.createNewAccount("kim");
        placeService.connectAccountPlace(kim,place);

        mockMvc.perform(get("/place/remove-user/kim/광주"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/place/management/광주"));

        assertFalse(accountPlaceRepository.existsByAccountUsernameAndPlaceId(kim.getUsername(), place.getId()));

    }
    @WithAccount(Value = "wrong")
    @DisplayName("장소에서 사용자 제거 - 생성자가 아닌 사용자의 요청")
    @Test
    void removeUser_invalid_userRequest() throws Exception {
        Account account = accountFactory.createNewAccount("bigave");
        Place place = placeFactory.createNewPlace("광주", account);

        Account kim = accountFactory.createNewAccount("kim");
        placeService.connectAccountPlace(kim,place);

        mockMvc.perform(get("/user/kim/광주"))
                .andExpect(status().is4xxClientError());

        assertTrue(accountPlaceRepository.existsByAccountUsernameAndPlaceId(kim.getUsername(), place.getId()));

    }




}