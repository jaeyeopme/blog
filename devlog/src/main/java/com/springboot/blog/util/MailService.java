package com.springboot.blog.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import javax.mail.internet.MimeMessage;

@Slf4j
@Service
public class MailService {

    private static final String ENCODING = "UTF-8";
    private static final String INVALID_EMAIL_MESSAGE = "Invalid email.";
    private static final String SEND_FAILED_MESSAGE = "Failed to send email.";

    private final JavaMailSender javaMailSender;

    public MailService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    public void send(String to, String subject, String text) {
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
    }

}
