package com.attendance.modules.settings;

import com.attendance.modules.account.Account;
import com.attendance.modules.account.AccountRepository;
import com.attendance.modules.settings.form.PasswordForm;
import com.attendance.modules.settings.form.ProfileForm;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Transactional
@RequiredArgsConstructor
@Service
public class SettingsService {
    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;


    public void changePassword(String username, PasswordForm passwordForm) {
        Account account = accountRepository.findByUsername(username);
        account.setPassword(passwordEncoder.encode(passwordForm.getNewPassword()));

    }

    public Account profileSetting(String username, ProfileForm profileForm) {
        Account account = accountRepository.findByUsername(username);

        modelMapper.map(profileForm, account);

        return account;
    }
}
