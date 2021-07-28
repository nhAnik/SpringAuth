package com.nhanik.springauth.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
public class EmailService {

    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);

    @Autowired
    JavaMailSender mailSender;

    @Async
    public void sendMail(String tokenUuid, String to) {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, "utf-8");
        String mailText = generateMailText(tokenUuid, to);
        try {
            helper.setTo(to);
            helper.setSubject("Reset password");
            helper.setText(mailText);
            mailSender.send(message);
        } catch (MessagingException e) {
            logger.error("Something wrong with the mail!");
        }
    }

    private String generateMailText(String tokenUuid, String to) {
        String userName = to.split("@")[0];
        String link = "http://localhost:8080/reset_password?reset_password_token="+tokenUuid;
        return "Hi " + userName + ",\n" +
                "Here is your password recovery link. Please follow the URL.\n" +
                link;

    }
}
