package com.attendance.modules.account;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@RequiredArgsConstructor
@Service
public class AccountService  {

    private final AccountRepository accountRepository;

    private final JavaMailSender javaMailSender;

    public void createNewAccount(SignUpForm signUpForm) {
        Account account = Account.builder()
                .email(signUpForm.getEmail())
                .password(signUpForm.getPassword())
                .build();

        account.generateEmailCheckToken();
        Account newAccount = accountRepository.save(account);

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
