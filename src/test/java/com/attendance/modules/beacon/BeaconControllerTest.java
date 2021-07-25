package com.attendance.modules.beacon;

import com.attendance.WithAccount;
import com.attendance.modules.account.Account;
import com.attendance.modules.account.AccountRepository;
import com.attendance.modules.account.AccountService;
import com.attendance.modules.account.form.SignUpForm;
import lombok.With;
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


import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class BeaconControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    BeaconRepository beaconRepository;

    @Autowired
    AccountRepository accountRepository;


    @Autowired
    AccountService accountService;

    @BeforeEach
    void initData(){
        beaconRepository.save(Beacon.builder()
                .location("광주")
                .beaconCode("aaaa-bbbb-cccc")
                .creator("bigave")
                .creationDate(LocalDateTime.now())
                .build());
    }

    @AfterEach
    void cleanup(){
        accountRepository.deleteAll();
        beaconRepository.deleteAll();
    }


    @WithAccount(Value = "bigave")
    @DisplayName("비콘 등록하기 화면")
    @Test
    void beaconView() throws Exception {

        mockMvc.perform(get("/add-beacon"))
                .andExpect(status().isOk())
                .andExpect(view().name("user/add-beacon"))
                .andExpect(model().attributeExists("beaconForm"));
    }

    @WithAccount(Value = "bigave")
    @DisplayName("비콘 등록 - 정상 입력")
    @Test
    void addBeacon() throws Exception {

        mockMvc.perform(post("/add-beacon")
                .param("beaconCode","asdf-asdf-aej2h3")
                .param("location", "test-location")
                .param("creator","bigave")
                .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"))
                .andExpect(model().hasNoErrors());

    }

    @WithMockUser
    @DisplayName("비콘 등록 - 이미 등록된 위치")
    @Test
    void addBeacon_duplicated_location() throws Exception {


        mockMvc.perform(post("/add-beacon")
                .param("beaconCode","asdf-asdf-aej2h3")
                .param("location", "광주")
                .param("creator","bigave")
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("user/add-beacon"))
                .andExpect(model().hasErrors());

    }

    @WithMockUser
    @DisplayName("비콘 등록 - 이미 등록된 비콘")
    @Test
    void addBeacon_duplicated_beacon() throws Exception {

        mockMvc.perform(post("/add-beacon")
                .param("beaconCode","aaaa-bbbb-cccc")
                .param("location", "test-location")
                .param("creator","bigave")
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("user/add-beacon"))
                .andExpect(model().hasErrors());

    }

    @WithAccount(Value = "bigave")
    @DisplayName("내가 등록한 비콘 페이지")
    @Test
    void myBeacon() throws Exception {
        mockMvc.perform(get("/beacon/my-beacon"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("beacons"));


    }

    @Transactional
    @WithAccount(Value = "bigave")
    @DisplayName("비콘 제거 - 생성자")
    @Test
    void removeBeacon_with_creator() throws Exception {

        mockMvc.perform(get("/beacon/remove/광주"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/beacon/my-beacon"));

        assertFalse(beaconRepository.existsByLocation("광주"));

    }

    @Transactional
    @WithAccount(Value = "other")
    @DisplayName("비콘 제거 - 생성자가 아님.")
    @Test
    void removeBeacon_wrong() throws Exception {

        mockMvc.perform(get("/beacon/remove/광주"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/error"));

        assertTrue(beaconRepository.existsByLocation("광주"));


    }

}