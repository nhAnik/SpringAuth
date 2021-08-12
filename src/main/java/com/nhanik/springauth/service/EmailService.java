package com.nhanik.springauth.service;

import com.nhanik.springauth.exception.MalformedEmailException;
import org.apache.commons.validator.routines.EmailValidator;
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
    public void sendMail(String tokenUuid, String to, boolean isConfirmation) {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, "utf-8");
        String mailText = generateMailText(tokenUuid, to, isConfirmation);
        String subject = isConfirmation ? "Confirm mail" : "Reset password";
        try {
            helper.setTo(to);
            helper.setFrom("no-reply@nhanik.com");
            helper.setSubject(subject);
            helper.setText(mailText);
            mailSender.send(message);
        } catch (MessagingException e) {
            logger.error("Something wrong with the mail!");
        }
    }

    private String generateMailText(String tokenUuid, String to, boolean isConfirmation) {
        String userName = to.split("@")[0];
        String mailBody = isConfirmation ? generateEmailConfirmationMailText(tokenUuid) :
                generatePasswordRecoveryMailText(tokenUuid);
        return "Hi " + userName + ",\n" + mailBody;
    }

    private String generateEmailConfirmationMailText(String tokenUuid) {
        String link = "http://localhost:8080/register/confirm?confirm_token="+tokenUuid;
        return "Here is your email confirmation link. Please follow the URL.\n" + link;
    }

    private String generatePasswordRecoveryMailText(String tokenUuid) {
        String link = "http://localhost:8080/reset_password?reset_password_token="+tokenUuid;
        return "Here is your password recovery link. Please follow the URL.\n" + link;
    }

    public void checkInvalidEmail(String email) {
        EmailValidator emailValidator = EmailValidator.getInstance();
        if (!emailValidator.isValid(email)) {
            throw new MalformedEmailException(email);
        }
    }
}
