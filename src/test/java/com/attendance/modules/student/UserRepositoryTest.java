package com.attendance.modules.student;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;


@DataJpaTest
class UserRepositoryTest {

    @Autowired
    UserRepository userRepository;

    @Test
    void save(){
        String username = "bigave";

        User user = User.builder()
                .username(username)
                .build();

        User result = userRepository.save(user);

        assertThat(result.getUsername()).isEqualTo(username);


    }



}