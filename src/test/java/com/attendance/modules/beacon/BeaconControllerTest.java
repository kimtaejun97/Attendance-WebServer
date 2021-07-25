package com.attendance.modules.beacon;

import com.attendance.WithAccount;
import com.attendance.modules.account.Account;
import com.attendance.modules.account.AccountService;
import com.attendance.modules.account.form.SignUpForm;
import lombok.With;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;


import static org.assertj.core.api.Assertions.assertThat;
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

    @MockBean
    BeaconRepository mockBeaconRepository;

    @Autowired
    AccountService accountService;

    @WithAccount(Value = "bigave")
    @DisplayName("비콘 등록하기 화면")
    @Test
    void beaconView() throws Exception {

        mockMvc.perform(get("/add-beacon"))
                .andExpect(status().isOk())
                .andExpect(view().name("user/add-beacon"))
                .andExpect(model().attributeExists("beaconForm"));
    }

    @WithMockUser
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

        when(mockBeaconRepository.existsByLocation("test-location")).thenReturn(true);

        mockMvc.perform(post("/add-beacon")
                .param("beaconCode","asdf-asdf-aej2h3")
                .param("location", "test-location")
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

        when(mockBeaconRepository.existsByBeaconCode("asdf-asdf-aej2h3")).thenReturn(true);

        mockMvc.perform(post("/add-beacon")
                .param("beaconCode","asdf-asdf-aej2h3")
                .param("location", "test-location")
                .param("creator","bigave")
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("user/add-beacon"))
                .andExpect(model().hasErrors());

    }


}