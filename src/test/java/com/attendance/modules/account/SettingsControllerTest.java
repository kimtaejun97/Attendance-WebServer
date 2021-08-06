package com.attendance.modules.account;

import com.attendance.WithAccount;
import com.attendance.modules.account.Account;
import com.attendance.modules.account.AccountRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;

@SpringBootTest
@AutoConfigureMockMvc
class SettingsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;



    @WithAccount(Value = "bigave")
    @DisplayName("프로필 수정 페이지")
    @Test
    void profileView() throws Exception {
        mockMvc.perform(get("/settings/profile"))
                .andExpect(status().isOk())
                .andExpect(view().name("settings/profile"))
                .andExpect(model().attributeExists("account"))
                .andExpect(model().attributeExists("profileForm"));

    }
    @WithAccount(Value = "bigave")
    @DisplayName("프로필 수정")
    @Test
    void profileUpdate() throws Exception {

        mockMvc.perform(post("/settings/profile")
                .param("phoneNumber", "01011111111")
                .param("address","광주 동구")
                .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/account/my-profile"));

        Account account = accountRepository.findByUsername("bigave");
        assertThat(account.getPhoneNumber()).isEqualTo("01011111111");
        assertThat(account.getAddress()).isEqualTo("광주 동구");
    }

    @WithAccount(Value = "bigave")
    @DisplayName("비밀번호 수정 페이지")
    @Test
    void passwordUpdateView() throws Exception {
        mockMvc.perform(get("/settings/password"))
                .andExpect(status().isOk())
                .andExpect(view().name("settings/account"))
                .andExpect(model().attributeExists("passwordForm"));

    }

    @WithAccount(Value = "bigave")
    @DisplayName("패스워드 수정 - 정상 입력")
    @Test
    void passwordUpdate() throws Exception {

        mockMvc.perform(post("/settings/password")
                .param("currentPassword", "123123123")
                .param("newPassword","11112222")
                .param("confirmPassword","11112222")
                .param("username","bigave")
                .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/settings/password"))
                .andExpect(flash().attributeExists("success"));

        Account account = accountRepository.findByUsername("bigave");
        assertTrue(passwordEncoder.matches("11112222",account.getPassword()));
    }

    @WithAccount(Value = "bigave")
    @DisplayName("패스워드 수정 - 이전 패스워드와 동일")
    @Test
    void passwordUpdate_prev_password() throws Exception {

        mockMvc.perform(post("/settings/password")
                .param("currentPassword", "123123123")
                .param("newPassword","123123123")
                .param("confirmPassword","123123123")
                .param("username","bigave")
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("settings/account"))
                .andExpect(model().hasErrors());

    }

    @WithAccount(Value = "bigave")
    @DisplayName("패스워드 수정 - 패스워드 확인 틀림")
    @Test
    void passwordUpdate_wrong_confirm() throws Exception {

        mockMvc.perform(post("/settings/password")
                .param("currentPassword", "123123123")
                .param("newPassword","11112222")
                .param("confirmPassword","22221111")
                .param("username","bigave")
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("settings/account"))
                .andExpect(model().hasErrors());

        Account account = accountRepository.findByUsername("bigave");
        assertTrue(passwordEncoder.matches("123123123",account.getPassword()));

    }

    @WithAccount(Value = "bigave")
    @DisplayName("계정 제거 - 올바른 입력")
    @Test
    void removeAccount_correct() throws Exception {
        mockMvc.perform(post("/account/remove")
                .param("confirmString", "bigave")
                .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/logout"));

        assertFalse(accountRepository.existsByUsername("bigave"));

    }

    @WithAccount(Value = "bigave")
    @DisplayName("계정 제거 - 잘못된 확인값 입력")
    @Test
    void removeAccount_wrong() throws Exception {
        mockMvc.perform(post("/account/remove")
                .param("confirmString", "wrong")
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("account/my-profile"))
                .andExpect(model().attributeExists("account"))
                .andExpect(model().attributeExists("error"));

        assertTrue(accountRepository.existsByUsername("bigave"));

    }

}