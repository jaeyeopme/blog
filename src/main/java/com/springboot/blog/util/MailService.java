package com.springboot.blog.util;

import org.springframework.http.HttpStatus;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import javax.mail.internet.MimeMessage;

import static com.springboot.blog.config.SecurityConfig.*;

@Service
public class MailService {

    private final JavaMailSender javaMailSender;

    public MailService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    public String send(String to, String subject, String text) {
        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, ENCODING);

            mimeMessageHelper.setTo(to);
            mimeMessageHelper.setSubject(subject);
            mimeMessageHelper.setText(text);

            javaMailSender.send(mimeMessage);
        } catch (AddressException addressException) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, INVALID_EMAIL_MESSAGE, addressException);
        } catch (MessagingException messagingException) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, SEND_FAILED_MESSAGE, messagingException);
        }

        return SEND_SUCCESS_MESSAGE;
    }

}
