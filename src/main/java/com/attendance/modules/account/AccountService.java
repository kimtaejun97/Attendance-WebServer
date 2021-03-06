package com.attendance.modules.account;

import com.attendance.infra.config.AppProperties;
import com.attendance.infra.mail.EmailMessage;
import com.attendance.infra.mail.EmailService;
import com.attendance.modules.account.form.SignUpForm;
import lombok.RequiredArgsConstructor;
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
                .subject("?????? ????????? ?????? ?????? ??????")
                .text(message)
                .build();
    }

    private String createHtmlMessage(Context context) {
        return templateEngine.process("mail/check-link", context);
    }
    private Context creteContext(Account account) {
        Context context = new Context();
        context.setVariable("title", "?????? ????????? ????????????");
        context.setVariable("username", account.getUsername());
        context.setVariable("host", appProperties.getHost());
        context.setVariable("linkName", "???????????? ??????");
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
            model.addAttribute("error","?????? ?????? ????????? ????????? ??? ??? ????????????. ???????????? ?????? ???????????????.");
        }else{
            resendEmail(account);
            model.addAttribute("success","?????? ????????? ????????? ????????????. ???????????? ??????????????????.");
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


    public void validateToken(String token, Account account) {
        if(!account.isValidToken(token)){
            throw new IllegalArgumentException("?????? ????????? ???????????? ????????????.");
        }
    }

    public Account findByUsername(String username) {
        Account account =  accountRepository.findByUsername(username);
        checkIfAccountNull(account);
        return account;
    }

    private void checkIfAccountNull(Account account) {
        if(account == null){
            throw new IllegalArgumentException("???????????? ?????? ????????? ?????????.");
        }
    }

    public Account findByEmail(String email) {
        Account account = accountRepository.findByEmail(email);
        checkIfAccountNull(account);
        return account;
    }
}
