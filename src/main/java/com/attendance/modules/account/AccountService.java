package com.attendance.modules.account;

import com.attendance.infra.config.AppProperties;
import com.attendance.infra.mail.EmailMessage;
import com.attendance.infra.mail.EmailService;
import com.attendance.modules.account.form.SignUpForm;
import com.attendance.modules.beacon.BeaconRepository;
import com.attendance.modules.place.PlaceRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.time.LocalDateTime;
import java.util.List;

@Transactional
@RequiredArgsConstructor
@Service
public class AccountService implements UserDetailsService{

    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;

    private final ModelMapper modelMapper;

    private final EmailService emailService;
    private final AppProperties appProperties;
    private final TemplateEngine templateEngine;


    public Account createNewAccount(SignUpForm signUpForm) {
        Role role = Role.USER;
        if(signUpForm.getAdminCode().equals("Admin1234")){
            role = Role.ADMIN;
        }
        signUpForm.setPassword(passwordEncoder.encode(signUpForm.getPassword()));
        signUpForm.setCreationDate(LocalDateTime.now());

        Account account = modelMapper.map(signUpForm, Account.class);
        account.setRole(role);
        account.generateEmailCheckToken();

        Account newAccount = accountRepository.save(account);
        sendEmail(newAccount);

        return newAccount;

    }

    private void sendEmail(Account account) {
        Context context = new Context();
        context.setVariable("title", "출입 시스템 회원인증");
        context.setVariable("username", account.getUsername());
        context.setVariable("host", appProperties.getHost());
        context.setVariable("linkName", "회원인증 하기");
        context.setVariable("link","/check-email-token?token="+account.getEmailCheckToken()
                +"&email="+account.getEmail());

        String message = templateEngine.process("mail/check-link",context);

        EmailMessage emailMessage = EmailMessage.builder()
                .to(account.getEmail())
                .subject("출입 시스템 회원 가입 인증")
                .text(message)
                .build();

        emailService.send(emailMessage);

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
            account = accountRepository.findByUsername(nameOrEmail);
        }

        if(account == null){
            throw new UsernameNotFoundException(nameOrEmail);
        }
        return new UserAccount(account);

    }

    public void resendCheckEmail(String username) {
        Account account = accountRepository.findByUsername(username);
        account.generateEmailCheckToken();

        sendEmail(account);
    }

    public void removeAccount(String username) {
        Account account = accountRepository.findByUsername(username);
        accountRepository.delete(account);




    }
}
