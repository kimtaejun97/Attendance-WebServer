package com.attendance.modules.account;

import com.attendance.infra.config.AppProperties;
import com.attendance.infra.mail.EmailMessage;
import com.attendance.infra.mail.EmailService;
import com.attendance.modules.account.form.SignUpForm;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.modelmapper.ModelMapper;
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
import org.springframework.ui.Model;
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


    public Account createAccount(SignUpForm signUpForm) {
        setPropertiesAndEncodePassword(signUpForm);
        Account account = modelMapper.map(signUpForm, Account.class);
        return saveAccountAndSendEmail(account);
    }
    private Account saveAccountAndSendEmail(Account account) {
        Account newAccount = saveAccount(account);
        sendAuthenticationEmail(newAccount);
        return newAccount;
    }
    private Account saveAccount(Account account) {
        account.generateEmailAuthenticationToken();
        Account newAccount = accountRepository.save(account);
        return newAccount;
    }

    private void setPropertiesAndEncodePassword(SignUpForm signUpForm) {
        setRole(signUpForm);
        setCreationDate(signUpForm);
        encodePassword(signUpForm);
    }
    private void encodePassword(SignUpForm signUpForm) {
        signUpForm.setPassword(passwordEncoder.encode(signUpForm.getPassword()));
    }
    private void setCreationDate(SignUpForm signUpForm) {
        signUpForm.setCreationDate(LocalDateTime.now());
    }
    private void setRole(SignUpForm signUpForm) {
        if(signUpForm.getAdminCode().equals("Admin1234")){
           signUpForm.setRole(Role.ADMIN);
        }else{
            signUpForm.setRole(Role.USER);
        }
    }

    private void sendAuthenticationEmail(Account account) {
        Context context = creteContext(account);
        EmailMessage emailMessage = createEmailMessage(account, context);
        emailService.send(emailMessage);
    }

    private EmailMessage createEmailMessage(Account account, Context context) {
        String message = createHtmlMessage(context);
        return EmailMessage.builder()
                .to(account.getEmail())
                .subject("출입 시스템 회원 가입 인증")
                .text(message)
                .build();
    }

    private String createHtmlMessage(Context context) {
        return templateEngine.process("mail/check-link", context);
    }
    private Context creteContext(Account account) {
        Context context = new Context();
        context.setVariable("title", "출입 시스템 회원인증");
        context.setVariable("username", account.getUsername());
        context.setVariable("host", appProperties.getHost());
        context.setVariable("linkName", "회원인증 하기");
        context.setVariable("link","/email-authentication-token?token="+ account.getEmailAuthenticationToken()
                +"&email="+ account.getEmail());
        return context;
    }

    public void completeSignUp(Account account) {
        account.completeSignUp();
        login(account);
    }

    public void login(Account account) {
        UsernamePasswordAuthenticationToken token = createUsernamePasswordAuthenticationToken(account);
        setInSecurityContext(token);
    }

    private void setInSecurityContext(UsernamePasswordAuthenticationToken token) {
        SecurityContext context = SecurityContextHolder.getContext();
        context.setAuthentication(token);
    }

    private UsernamePasswordAuthenticationToken createUsernamePasswordAuthenticationToken(Account account) {
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                new UserAccount(account),
                account.getPassword(),
                List.of(new SimpleGrantedAuthority(account.getRole().getKey()))
        );
        return token;
    }

    @Override
    public UserDetails loadUserByUsername(String nameOrEmail) throws UsernameNotFoundException {
        Account account = accountRepository.findByNameOrEmail(nameOrEmail);
        checkIfAccountExists(nameOrEmail, account);
        return new UserAccount(account);
    }

    private void checkIfAccountExists(String nameOrEmail, Account account) {
        if(account == null){
            throw new UsernameNotFoundException(nameOrEmail);
        }
    }

    public void resendAuthenticationEmail(String username, Model model) {
        Account account = accountRepository.findByUsername(username);

        if(!account.canSendEmail()){
            model.addAttribute("error","아직 인증 메일을 재전송 할 수 없습니다. 잠시후에 다시 시도하세요.");
        }else{
            resendEmail(account);
            model.addAttribute("success","인증 메일을 재전송 했습니다. 메일함을 확인해주세요.");
        }
    }

    private void resendEmail(Account account) {
        account.generateEmailAuthenticationToken();
        sendAuthenticationEmail(account);
    }

    public void removeAccount(String username) {
        Account account = accountRepository.findByUsername(username);
        accountRepository.delete(account);
    }

    public void validateAuthentication(Account account, String token) {
        validateAccount(account);
        validateToken(token, account);
    }

    private void validateToken(String token, Account account) {
        if(!account.isValidToken(token)){
            throw new IllegalArgumentException("인증 토큰이 유효하지 않습니다.");
        }
    }
    private void validateAccount(Account account) {
        if(account == null){
            throw new IllegalArgumentException("이메일 정보가 올바르지 않습니다.");
        }
    }

    public Account findByUsername(String username) {
        return accountRepository.findByUsername(username);
    }

    public Account findByEmail(String email) {
        return accountRepository.findByEmail(email);
    }
}
