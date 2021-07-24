package com.attendance.modules.place;

import com.attendance.WithAccount;
import com.attendance.modules.account.AccountRepository;
import com.attendance.modules.account.AccountService;
import com.attendance.modules.beacon.Beacon;
import com.attendance.modules.beacon.BeaconRepository;
import com.attendance.modules.place.form.PlaceForm;
import com.attendance.modules.userplace.AccountPlaceRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
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

    @BeforeEach
    void initData(){
        beaconRepository.save(Beacon.builder()
                .location("광주")
                .beaconCode("123df-3fsdf3-dsaf-3")
                .creator("bigave")
                .build());

        PlaceForm placeForm = new PlaceForm();
        placeForm.setLocation("광주");
        placeForm.setAlias("내 지역");
        placeForm.setCreator("bigave");

        placeService.createPlace(placeForm, "on");
    }

    @AfterEach
    void cleanup(){
        accountPlaceRepository.deleteAll();
        placeRepository.deleteAll();
        accountRepository.deleteAll();
        beaconRepository.deleteAll();
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
        beaconRepository.save(Beacon.builder()
                .location("광주2")
                .beaconCode("1df-3fsdf3-dsaf-3")
                .creator("bigave")
                .build());

        mockMvc.perform(post("/create-place")
        .param("location", "광주2")
        .param("alias", "내 지역")
        .param("creator", "bigave")
        .param("isPublic", "on")
        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/"));

        assertTrue(placeRepository.existsByLocation("광주"));
    }

    @WithMockUser
    @DisplayName("장소 추가 - 입력값 오류 : 존재하지 않는 비콘위치")
    @Test
    void createPlace_with_wrong_input() throws Exception {

        mockMvc.perform(post("/create-place")
                .param("location", "nonono")
                .param("alias", "내 지역")
                .param("creator", "bigave")
                .param("isPublic", "on")
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("user/create-place"));

        assertFalse(placeRepository.existsByLocation("nonono"));
    }

    @WithMockUser
    @DisplayName("장소 생성 - 이미 생성되어 있는 위치")
    @Test
    void createPlace_with_exists_input() throws Exception {


        mockMvc.perform(post("/create-place")
                .param("location", "광주")
                .param("alias", "내 지역")
                .param("creator", "bigave")
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

        mockMvc.perform(get("/admin/place/광주"))
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


    @WithMockUser
    @Test
    @DisplayName("장소 제거")
    void removePlace() throws Exception {

        Place place = placeRepository.findByLocation("광주");
        assertNotNull(place);

        mockMvc.perform(get("/place/remove-place/광주"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/my-place"));

        assertFalse(placeRepository.existsByLocation("광주"));
    }

    @WithAccount(Value = "bigave")
    @Test
    @DisplayName("내가 생성한 장소 관리 페이지")
    void myPlaceManagement() throws Exception {

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

        mockMvc.perform(get("/place/management/광주"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/error"));

    }





}