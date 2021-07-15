package com.attendance.modules.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UsersRepository extends JpaRepository<Users, Long> {

    @Query("select S.username from Users S where S.username like ?1")
    String findByUsername(String username);

}
