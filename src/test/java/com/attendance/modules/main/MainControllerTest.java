package com.attendance.modules.main;

import com.attendance.modules.account.*;
import com.attendance.modules.account.form.SignUpForm;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.unauthenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
class MainControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    AccountService accountService;

    @BeforeEach
    void initDate(){
        SignUpForm signUpForm = new SignUpForm();
        signUpForm.setEmail("test@email.com");
        signUpForm.setUsername("bigave");
        signUpForm.setPassword("12345678");

        accountService.createNewAccount(signUpForm);

    }

    @AfterEach
    void cleaup(){
        accountRepository.deleteAll();
    }

    @DisplayName("로그인 - 정상 입력")
    @Test
    void login_with_correct_input() throws Exception {

        mockMvc.perform(post("/login")
                .param("username","bigave")
                .param("password","12345678")
                .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"))
                .andExpect(authenticated().withUsername("bigave"));
    }

    @DisplayName("로그인 - 잘못된 입력")
    @Test
    void login_with_worng_input() throws Exception {

        mockMvc.perform(post("/login")
                .param("username","bigave22")
                .param("password","12345678")
                .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login?error"))
                .andExpect(unauthenticated());
    }

    @WithMockUser
    @DisplayName("로그아웃")
    @Test
    void logout() throws Exception {

        mockMvc.perform(post("/logout")
                .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"))
                .andExpect(unauthenticated());
    }

}