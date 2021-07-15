package com.attendance.infra.config;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;


@RequiredArgsConstructor
@EnableWebSecurity
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception {


        http
                .csrf().disable()
                .headers().frameOptions().disable()
                        .and()
        .authorizeRequests()
                .antMatchers("/","/sign-up","/login", "/check-email-token","/h2-console/**").permitAll()
                .anyRequest().authenticated();


        http.formLogin()
                .loginPage("/login").permitAll();

        http.logout().logoutSuccessUrl("/");
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring()
                .requestMatchers((PathRequest.toStaticResources().atCommonLocations()));

    }
}
