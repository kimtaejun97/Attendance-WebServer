package com.attendance.infra.mail;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("test")
@Slf4j
public class ConsoleEmailService implements EmailService {

    @Override
    public void send(EmailMessage emailMessage) {
        log.info("Console Email : " + emailMessage.getText());
    }
}
