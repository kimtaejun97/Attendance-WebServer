package com.attendance.modules.userplace;

import com.attendance.WithAccount;
import com.attendance.modules.account.Account;
import com.attendance.modules.account.AccountRepository;
import com.attendance.modules.account.AccountService;
import com.attendance.modules.account.Role;
import com.attendance.modules.place.Place;
import com.attendance.modules.place.PlaceRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;

@SpringBootTest
@AutoConfigureMockMvc
class AccountPlaceControllerTest {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    PlaceRepository placeRepository;

    @Autowired
    AccountPlaceRepository accountPlaceRepository;

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    AccountService accountService;

    @Autowired
    AccountPlaceService accountPlaceService;

    @BeforeEach
    void initData(){
        placeRepository.save(
                Place.builder()
                        .location("광주")
                        .alias("광주")
                        .creator("bigave")
                        .isPublic("on")
                        .build()
        );
    }

    @AfterEach
    void cleanup(){
        accountPlaceRepository.deleteAll();
        placeRepository.deleteAll();
        accountRepository.deleteAll();
    }

    @WithAccount(Value = "bigave")
    @DisplayName("생성자 :: 사용자 추가 화면")
    @Test
    void addUserView() throws Exception {

        mockMvc.perform(get("/add-user/광주"))
                .andExpect(status().isOk())
                .andExpect(view().name("user/add-user"))
                .andExpect(model().attributeExists("userForm"));
    }

    @WithAccount(Value = "bigave")
    @DisplayName("생성자 :: 사용자 추가 - 정상 입력")
    @Test
    void addUser_with_correct_input() throws Exception {


        mockMvc.perform(post("/place/add-user/광주")
                .param("username","bigave")
                .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/user/place/광주"))
                .andExpect(model().attributeDoesNotExist("errors"));

        Account account = accountRepository.findByUsername("bigave");
        Place place = placeRepository.findByLocation("광주");

        boolean isRegistered = accountPlaceRepository.existsByAccountAndPlace(account,place);

        assertTrue(isRegistered);


    }
    @WithAccount(Value = "bigave")
    @DisplayName("생성자 :: 사용자 추가 - 존재하지 않는 사용자")
    @Test
    void addUser_with_nonexist_user() throws Exception {

        mockMvc.perform(post("/place/add-user/광주")
                .param("username","nonono")
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
        Place place = placeRepository.findByLocation("광주");

        accountPlaceRepository.save(AccountPlace.builder()
                .account(account)
                .place(place)
                .build());

        mockMvc.perform(post("/place/add-user/광주")
                .param("username","bigave")
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("user/add-user"))
                .andExpect(model().hasErrors());

    }

    @WithAccount(Value = "bigave")
    @DisplayName("공개된 장소 :: 나를 추가")
    @Test
    void publicPlace_addMe() throws Exception {

        mockMvc.perform(get("/public-place/enrollment/광주")
                .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/my-place"));
    }
    @WithAccount(Value = "kim")
    @DisplayName("장소에서 탈퇴.")
    @Test
    void disconnectPlace() throws Exception {
        accountPlaceService.connectUserPlace("kim","광주");

        mockMvc.perform(get("/account-place/disconnect-place/광주"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/my-place"));
        Account account = accountRepository.findByUsername("kim");
        assertFalse(accountPlaceRepository.existsByAccountIdAndPlaceLocation(account.getId(), "광주"));
    }



    @WithAccount(Value = "bigave")
    @DisplayName("장소에서 사용자 제거")
    @Test
    void removeUser() throws Exception {
        Account kim = accountRepository.save(Account.builder()
                .username("kim")
                .password("123123123")
                .email("test22@eee.eee")
                .role(Role.USER)
                .build());

        accountPlaceService.connectUserPlace("kim","광주");

        mockMvc.perform(get("/account-place/remove-user/kim/광주"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/place/management/광주"));

        assertFalse(accountPlaceRepository.existsByAccountIdAndPlaceLocation(kim.getId(), "광주"));

    }
    @WithAccount(Value = "wrong")
    @DisplayName("장소에서 사용자 제거 - 생성자가 아닌 사용자의 요청.")
    @Test
    void removeUser_invalid_userRequest() throws Exception {
        Account kim = accountRepository.save(Account.builder()
                .username("kim")
                .password("123123123")
                .email("test22@eee.eee")
                .role(Role.USER)
                .build());

        accountPlaceService.connectUserPlace("kim","광주");

        mockMvc.perform(get("/account-place/remove-user/kim/광주"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/error"));

        assertTrue(accountPlaceRepository.existsByAccountIdAndPlaceLocation(kim.getId(), "광주"));

    }

}