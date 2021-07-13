package com.attendance.modules.account;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
public interface AccountRepository extends JpaRepository<Account,Long> {

    boolean existsByEmail(String email);

    Account findByEmail(String email);

    boolean existsByNickname(String nickname);

    Account findByNickname(String username);
}
