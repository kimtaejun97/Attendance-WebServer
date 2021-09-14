package com.attendance.modules.account;

import com.attendance.modules.account.Account;
import com.attendance.modules.account.AccountRepository;
import com.attendance.modules.account.form.PasswordForm;
import com.attendance.modules.account.form.ProfileForm;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Transactional
@RequiredArgsConstructor
@Service
public class SettingsService {
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;
    private final AccountService accountService;


    public void changePassword(Account account, PasswordForm passwordForm) {
        Account persistenceAccount = accountService.findByUsername(account.getUsername());
        persistenceAccount.setPassword(encodePassword(passwordForm.getNewPassword()));
    }

    private String encodePassword(String newPassword) {
        return passwordEncoder.encode(newPassword);
    }

    public void setProfile(Account account, ProfileForm profileForm) {
        Account persistenceAccount = accountService.findByUsername(account.getUsername());
        persistenceAccount.setProfile(profileForm);
    }
}
