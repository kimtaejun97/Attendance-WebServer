package com.attendance.modules.account;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@RequiredArgsConstructor
@Service
public class AccountService implements UserDetailsService{

    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;
    private final JavaMailSender javaMailSender;

    public Account createNewAccount(SignUpForm signUpForm) {
        Role role = Role.USER;
        if(signUpForm.getAdminCode() != ""){
            role = Role.ADMIN;
        }

        Account account = Account.builder()
                .nickname(signUpForm.getNickname())
                .email(signUpForm.getEmail())
                .password(passwordEncoder.encode(signUpForm.getPassword()))
                .role(role)
                .build();

        Account newAccount = accountRepository.save(account);
        account.generateEmailCheckToken();

        sendEmail(newAccount);

        return newAccount;

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
        login(account);
    }

    public void login(Account account) {
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                new UserAccount(account),
                account.getPassword(),
                List.of(new SimpleGrantedAuthority(account.getRole().getKey()))
        );

        SecurityContext context = SecurityContextHolder.getContext();
        context.setAuthentication(token);

    }

    @Override
    public UserDetails loadUserByUsername(String nameOrEmail) throws UsernameNotFoundException {
        Account account = accountRepository.findByEmail(nameOrEmail);

        if(account == null){
            account = accountRepository.findByNickname(nameOrEmail);
        }
        if(account == null){
            throw new UsernameNotFoundException(nameOrEmail);
        }
        System.out.println(account.getNickname());
        return new UserAccount(account);

    }
}
