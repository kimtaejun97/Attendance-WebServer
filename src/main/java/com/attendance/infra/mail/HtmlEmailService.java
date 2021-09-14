package com.attendance.infra.mail;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Profile("real")
@RequiredArgsConstructor
@Slf4j
@Component
public class HtmlEmailService implements EmailService{

    private final JavaMailSender javaMailSender;

    @Override
    public void send(EmailMessage emailMessage) {
        try {
            MimeMessage mimeMessage = makeMimeMessage(emailMessage);
            javaMailSender.send(mimeMessage);

            log.info("메일 전송 : {}",emailMessage.getTo());
        }catch (MessagingException e){
            log.error("메일 전송 실패 : ",e);
        }


    }

    private MimeMessage makeMimeMessage(EmailMessage emailMessage) throws MessagingException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        setMessageContents(emailMessage, mimeMessage);

        return mimeMessage;
    }

    private void setMessageContents(EmailMessage emailMessage, MimeMessage mimeMessage) throws MessagingException {
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, false,"UTF-8");
        mimeMessageHelper.setTo(emailMessage.getTo());
        mimeMessageHelper.setSubject(emailMessage.getSubject());
        mimeMessageHelper.setText(emailMessage.getText(),true);
    }
}
