package com.attendance.modules.user;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;


@DataJpaTest
class UsersRepositoryTest {

    @Autowired
    UsersRepository usersRepository;

    @Test
    void save(){
        String username = "bigave";

        Users users = Users.builder()
                .username(username)
                .build();

        Users result = usersRepository.save(users);

        assertThat(result.getUsername()).isEqualTo(username);


    }



}