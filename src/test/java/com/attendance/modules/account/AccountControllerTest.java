package com.attendance.modules.account;

import com.attendance.WithAccount;
import com.attendance.infra.mail.EmailMessage;
import com.attendance.infra.mail.EmailService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;


import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.then;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Transactional
@SpringBootTest
@AutoConfigureMockMvc
class AccountControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    AccountService accountService;

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    AccountFactory accountFactory;

    @MockBean
    EmailService emailService;


    @BeforeEach
    void initDate(){
        Account account = Account.builder()
                .username("bigave2")
                .email("init@email.com")
                .password("12345678")
                .creationDate(LocalDateTime.now())
                .build();
        Account newAccount = accountRepository.save(account);
        newAccount.generateEmailAuthenticationToken();
        newAccount.setRole(Role.USER);


    }

    @AfterEach
    void cleanup(){

        accountRepository.deleteAll();
    }


    @DisplayName("?????? ?????? View")
    @Test
    void signUpPage() throws Exception {
        mockMvc.perform(get("/sign-up"))
                .andExpect(status().isOk())
                .andExpect(view().name("account/sign-up"))
                .andExpect(model().attributeExists("signUpForm"));
    }


    @DisplayName("?????? ?????? - ????????? ??????")
    @Test
    void signUp_with_wrong_input() throws Exception {

        mockMvc.perform(post("/sign-up")
        .param("username","bigave")
        .param("email","aasdf")
        .param("password","asd")
        .param("adminCode","")
        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("account/sign-up"));


        // ????????? ??????
        mockMvc.perform(post("/sign-up")
                .param("username","bigave")
                .param("email","test@email.com")
                .param("password","123123123")
                .param("adminCode","wrong")
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("account/sign-up"));
    }

    @DisplayName("?????? ?????? - ?????? ??????")
    @Test
    void signUp_with_correct_input() throws Exception {

        mockMvc.perform(post("/sign-up")
            .param("username","test")
            .param("email","test@your.com")
            .param("password","123123123")
            .param("adminCode","")
            .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/"))
                .andExpect(authenticated());


        Account account = accountRepository.findByEmail("test@your.com");
        assertThat(account.getUsername()).isEqualTo("test");
        assertNotNull(account);
        assertNotEquals(account.getPassword(),"123123123");
        assertNotNull(account.getEmailAuthenticationToken());
        assertThat(account.getRole()).isEqualTo(Role.USER);


        then(emailService).should().send(any(EmailMessage.class));


    }
    @DisplayName("?????? ?????? - ???????????? : ?????????")
    @Test
    void signUp_with_correct_AdminCode() throws Exception {

        mockMvc.perform(post("/sign-up")
            .param("username","test")
            .param("email","test@your.com")
            .param("password","123123123")
            .param("adminCode","Admin1234")
            .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/"))
                .andExpect(authenticated());


        Account account = accountRepository.findByEmail("test@your.com");
        assertThat(account.getUsername()).isEqualTo("test");
        assertNotNull(account);
        assertNotEquals(account.getPassword(),"123123123");
        assertNotNull(account.getEmailAuthenticationToken());
        assertThat(account.getRole()).isEqualTo(Role.ADMIN);

        then(emailService).should().send(any(EmailMessage.class));

    }
    @DisplayName("?????? ?????? - ????????????")
    @Test
    void signUp_with_duplicate() throws Exception {

        //?????? ?????????.
        mockMvc.perform(post("/sign-up")
                .param("username","bigave2")
                .param("email","init2@email.com")
                .param("password","123123123")
                .param("adminCode","Admin1234")
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("account/sign-up"));

        //?????? ?????????
        mockMvc.perform(post("/sign-up")
                .param("username","duplicated")
                .param("email","init@email.com")
                .param("password","123123123")
                .param("adminCode","Admin1234")
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("account/sign-up"));

    }
    @DisplayName("?????? ?????? - ????????? ??????")
    @Test
    void authenticationToken_with_wrong_input() throws Exception {

        mockMvc.perform(get("/email-authentication-token")
                .param("token","wrong")
                .param("email","test@email.com"))
                .andExpect(status().isBadRequest())
                .andExpect(view().name("/error/4xx"));
    }

    @DisplayName("?????? ?????? - ?????? ??????")
    @Test
    void authenticationToken_with_correct_input() throws Exception {
        Account account = accountFactory.createNewAccount("bigave");
        Account newAccount = accountRepository.save(account);
        newAccount.generateEmailAuthenticationToken();
        newAccount.setRole(Role.USER);

        mockMvc.perform(get("/email-authentication-token")
        .param("token",newAccount.getEmailAuthenticationToken())
        .param("email",newAccount.getEmail()))
                .andExpect(status().isOk())
                .andExpect(view().name("account/checked-email"));
    }


    @WithAccount(Value ="bigave")
    @DisplayName("?????? ??????")
    @Test
    void checkEmail() throws Exception {

        mockMvc.perform(get("/check-email"))
                .andExpect(status().isOk())
                .andExpect(view().name("account/check-email"))
                .andExpect(model().attributeExists("email"));
    }

    @WithAccount(Value ="bigave")
    @DisplayName("?????? ???????????? ????????? - ????????? ??????(??????)")
    @Test
    void resendCheckEmail_invalid() throws Exception {

        mockMvc.perform(get("/resend-authentication-email"))
                .andExpect(status().isOk())
                .andExpect(view().name("account/check-email"))
                .andExpect(model().attributeExists("email"))
                .andExpect(model().attributeExists("error"));
    }

    @Transactional
    @WithAccount(Value ="bigave")
    @DisplayName("?????? ???????????? ????????? - ????????? ??????")
    @Test
    void resendCheckEmail() throws Exception {
        Account account = accountRepository.findByUsername("bigave");
        account.setEmailTokenLastGeneration(LocalDateTime.now().minusMinutes(20));

        mockMvc.perform(get("/resend-authentication-email"))
                .andExpect(status().isOk())
                .andExpect(view().name("account/check-email"))
                .andExpect(model().attributeExists("email"))
                .andExpect(model().attributeExists("success"));
    }




    @WithAccount(Value = "bigave")
    @DisplayName("?????? ????????? ?????????")
    @Test
    void myProfile() throws Exception {

        mockMvc.perform(get("/account"))
                .andExpect(status().isOk())
                .andExpect(view().name("account/my-profile"))
                .andExpect(model().attributeExists("account"));

    }



}