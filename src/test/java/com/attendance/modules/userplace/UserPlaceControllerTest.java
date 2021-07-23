package com.attendance.modules.userplace;

import com.attendance.WithAccount;
import com.attendance.modules.account.Account;
import com.attendance.modules.account.AccountRepository;
import com.attendance.modules.account.AccountService;
import com.attendance.modules.account.form.SignUpForm;
import com.attendance.modules.place.Place;
import com.attendance.modules.place.PlaceRepository;
import com.attendance.modules.place.form.PlaceForm;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;

@SpringBootTest
@AutoConfigureMockMvc
class UserPlaceControllerTest {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    PlaceRepository placeRepository;

    @Autowired
    UserPlaceRepository userPlaceRepository;

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    AccountService accountService;

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
        userPlaceRepository.deleteAll();
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

        boolean isRegistered = userPlaceRepository.existsByAccountAndPlace(account,place);

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

        userPlaceRepository.save(UserPlace.builder()
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

}