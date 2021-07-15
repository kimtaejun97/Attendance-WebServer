package com.attendance.modules.student;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends JpaRepository<User, Long> {

    @Query("select S.username from User S where S.username like ?1")
    String findByUsername(String username);

}
