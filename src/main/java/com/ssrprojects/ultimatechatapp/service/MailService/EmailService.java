package com.ssrprojects.ultimatechatapp.service.MailService;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender javaMailSender;

    @Value("${spring.mail.host}")
    private String host;

    public EmailService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    public boolean sendTextEmail(String subject, String body, String... recipient) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(recipient);
        message.setSubject(subject);
        message.setText(body);

        try {
            javaMailSender.send(message);
        } catch (MailException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    public boolean sendHtmlEmail(String subject, String htmlContent, String... recipient) {
        MimeMessage message = javaMailSender.createMimeMessage();

        try {
            MimeMessageHelper messageHelper = new MimeMessageHelper(message, true);

            messageHelper.setFrom(host);
            messageHelper.setTo(recipient);
            messageHelper.setSubject(subject);

            messageHelper.setText(htmlContent, true);
        } catch (MessagingException e) {
            e.printStackTrace();
            return false;
        }

        try {
            javaMailSender.send(message);
        } catch (MailException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

}
