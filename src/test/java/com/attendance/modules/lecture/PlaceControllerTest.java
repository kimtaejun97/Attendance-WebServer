package com.attendance.modules.lecture;

import com.attendance.modules.account.Account;
import com.attendance.modules.account.AccountRepository;
import com.attendance.modules.account.AccountService;
import com.attendance.modules.account.form.SignUpForm;
import com.attendance.modules.beacon.Beacon;
import com.attendance.modules.beacon.BeaconRepository;
import com.attendance.modules.lecture.form.PlaceForm;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Transactional
@SpringBootTest
@AutoConfigureMockMvc
class PlaceControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    PlaceService placeService;

    @Autowired
    PlaceRepository placeRepository;

    @MockBean
    PlaceRepository mockPlaceRepository;

    @MockBean
    BeaconRepository mockBeaconRepository;

    @Autowired
    AccountService accountService;

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    BeaconRepository beaconRepository;

    @AfterEach
    void cleanup(){
        placeRepository.deleteAll();
        accountRepository.deleteAll();
    }



    @WithMockUser
    @DisplayName("admin 페이지")
    @Test
    void adminPage() throws Exception {

        mockMvc.perform(get("/admin-page"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/admin-page"))
                .andExpect(model().attributeExists("places"));
    }

    @DisplayName("장소 추가 View")
    @Test
    void addLectureView() throws Exception {
        SignUpForm signUpForm = new SignUpForm();
        signUpForm.setNickname("bigave");
        signUpForm.setAdminCode("");
        signUpForm.setPassword("123123123");
        signUpForm.setEmail("test@email.com");

        Account newAccount = accountService.createNewAccount(signUpForm);
        accountService.login(newAccount);

        mockMvc.perform(get("/create-place"))
                .andExpect(status().isOk())
                .andExpect(view().name("user/create-place"))
                .andExpect(model().attributeExists("placeForm"));
    }

    @WithMockUser
    @DisplayName("장소 추가 - 입력값 정상")
    @Test
    void addLecture_with_correct_input() throws Exception {

        when(mockBeaconRepository.existsByLocation("광주")).thenReturn(true);

        mockMvc.perform(post("/create-place")
        .param("location", "광주")
        .param("alias", "내 지역")
        .param("constructor", "bigave")
        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/"));
    }

    @WithMockUser
    @DisplayName("장소 추가 - 입력값 오류 : 존재하지 않는 비콘위치")
    @Test
    void addLecture_with_wrong_input() throws Exception {

        when(mockBeaconRepository.existsByLocation("nonono")).thenReturn(false);

        mockMvc.perform(post("/create-place")
                .param("location", "nonono")
                .param("alias", "내 지역")
                .param("constructor", "bigave")
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("user/create-place"));
    }

    @WithMockUser
    @DisplayName("강의 추가 - 이미 생성되어 있는 위치")
    @Test
    void addLecture_with_exists_input() throws Exception {

        when(mockPlaceRepository.existsByLocation("광주")).thenReturn(true);

        mockMvc.perform(post("/create-place")
                .param("location", "광주")
                .param("alias", "내 지역")
                .param("constructor", "bigave")
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("user/create-place"));
    }

    @WithMockUser
    @DisplayName("admin - 장소 정보 View")
    @Test
    void adminPlaceInfo() throws Exception {
        PlaceForm placeForm = new PlaceForm();
        placeForm.setLocation("광주");
        placeForm.setAlias("내 지역");
        placeForm.setConstructor("bigave");

        when(mockPlaceRepository.findByLocation("광주")).thenReturn(placeForm.toEntity());

        mockMvc.perform(get("/admin/place/광주"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/place"))
                .andExpect(model().attributeExists("place"))
                .andExpect(model().attributeExists("users"));
    }

    @DisplayName("나의 강의 페이지")
    @Test
    void myLecture() throws Exception {
        SignUpForm signUpForm = new SignUpForm();
        signUpForm.setNickname("bigave");
        signUpForm.setAdminCode("");
        signUpForm.setPassword("123123123");
        signUpForm.setEmail("test@email.com");

        Account newAccount = accountService.createNewAccount(signUpForm);
        accountService.login(newAccount);

        mockMvc.perform(get("/my-place"))
                .andExpect(status().isOk())
                .andExpect(view().name("user/my-place"))
                .andExpect(model().attributeExists("places"));
    }





}