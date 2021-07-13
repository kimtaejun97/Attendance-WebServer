package com.attendance.modules.account;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@RequiredArgsConstructor
@Service
public class AccountService  {

    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;
    private final JavaMailSender javaMailSender;

    public void createNewAccount(SignUpForm signUpForm) {
        Account account = Account.builder()
                .nickname(signUpForm.getNickname())
                .email(signUpForm.getEmail())
                .password(passwordEncoder.encode(signUpForm.getPassword()))
                .build();

        Account newAccount = accountRepository.save(account);
        account.generateEmailCheckToken();

        sendEmail(newAccount);

    }

    private void sendEmail(Account account) {

        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setTo(account.getEmail());
        simpleMailMessage.setSubject("출석 시스템 회원 가입 인증");
        simpleMailMessage.setText("/check-email-token?token="+account.getEmailCheckToken()
        +"&email="+account.getEmail());

        javaMailSender.send(simpleMailMessage);

    }

    public void completeSignUp(Account account) {
        account.completeSignUp();
    }
}
