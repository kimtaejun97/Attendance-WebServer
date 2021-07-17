package com.attendance.modules.account;

import com.attendance.modules.account.form.SignUpForm;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;


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

    @MockBean
    JavaMailSender javaMailSender;


    @BeforeEach
    void initDate(){
        Account account = Account.builder()
                .username("bigave")
                .email("test@email.com")
                .password("12345678")
                .build();
        Account newAccount = accountRepository.save(account);
        newAccount.generateEmailCheckToken();
        newAccount.setRole(Role.USER);


    }

    @AfterEach
    void cleaup(){
        accountRepository.deleteAll();
    }


    @DisplayName("회원 가입 View")
    @Test
    void signUpPage() throws Exception {
        mockMvc.perform(get("/sign-up"))
                .andExpect(status().isOk())
                .andExpect(view().name("account/sign-up"))
                .andExpect(model().attributeExists("signUpForm"));
    }


    @DisplayName("회원 가입 - 잘못된 입력")
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


        // 관리자 코드
        mockMvc.perform(post("/sign-up")
                .param("username","bigave")
                .param("email","test@email.com")
                .param("password","123123123")
                .param("adminCode","wrong")
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("account/sign-up"));
    }

    @DisplayName("회원 가입 - 정상 입력")
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
        assertNotNull(account.getEmailCheckToken());
        assertThat(account.getRole()).isEqualTo(Role.USER);


        then(javaMailSender).should().send(any(SimpleMailMessage.class));


    }
    @DisplayName("회원 가입 - 정상입력 : 관리자")
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
        assertNotNull(account.getEmailCheckToken());
        assertThat(account.getRole()).isEqualTo(Role.ADMIN);

        then(javaMailSender).should().send(any(SimpleMailMessage.class));

    }
    @DisplayName("회원 가입 - 중복가입")
    @Test
    void signUp_with_duplicate() throws Exception {

        //중복 닉네임.
        mockMvc.perform(post("/sign-up")
                .param("username","bigave")
                .param("email","test2@email.com")
                .param("password","123123123")
                .param("adminCode","Admin1234")
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("account/sign-up"));

        //중복 이메일
        mockMvc.perform(post("/sign-up")
                .param("username","bigave2")
                .param("email","test@email.com")
                .param("password","123123123")
                .param("adminCode","Admin1234")
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("account/sign-up"));

    }
    @DisplayName("인증 메일 - 잘못된 입력")
    @Test
    void checkEmailToken_with_wrong_input() throws Exception {


        mockMvc.perform(get("/check-email-token")
                .param("token","asdasd")
                .param("email","test@email.com"))
                .andExpect(status().isOk())
                .andExpect(view().name("account/checked-email"))
                .andExpect(model().attributeExists("error"));

    }

    @DisplayName("인증 메일 - 정상 입력")
    @Test
    void checkEmailToken_with_correct_input() throws Exception {
        Account account = Account.builder()
                .username("test")
                .email("test@your.com")
                .password("12345678")
                .build();
        Account newAccount = accountRepository.save(account);
        newAccount.generateEmailCheckToken();
        newAccount.setRole(Role.USER);

        mockMvc.perform(get("/check-email-token")
        .param("token",newAccount.getEmailCheckToken())
        .param("email",newAccount.getEmail()))
                .andExpect(status().isOk())
                .andExpect(view().name("account/checked-email"))
                .andExpect(model().attributeDoesNotExist("error"));
    }


    @DisplayName("나의 프로필 화면")
    @Test
    void myProfile() throws Exception {
        SignUpForm signUpForm = new SignUpForm();
        signUpForm.setUsername("bigave2");
        signUpForm.setAdminCode("");
        signUpForm.setPassword("123123123");
        signUpForm.setEmail("test2@email.com");

        Account newAccount = accountService.createNewAccount(signUpForm);
        accountService.login(newAccount);

        mockMvc.perform(get("/my-profile"))
                .andExpect(status().isOk())
                .andExpect(view().name("account/my-profile"))
                .andExpect(model().attributeExists("account"));

    }



}