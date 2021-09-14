package com.attendance.modules.account;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
public interface AccountRepository extends JpaRepository<Account,String> {

    boolean existsByEmail(String email);

    Account findByEmail(String email);

    boolean existsByUsername(String nickname);

    Account findByUsername(String username);


    @Query("select A from Account A where A.username like ?1 or A.email like ?1")
    Account findByNameOrEmail(String nameOrEmail);
}
