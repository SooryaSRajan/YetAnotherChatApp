package com.ssrprojects.ultimatechatapp.service.MailService;

import com.ssrprojects.ultimatechatapp.entity.User;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.util.Arrays;

@Service
@Slf4j
public class EmailService {

    private final JavaMailSender javaMailSender;
    private final SpringTemplateEngine templateEngine;

    @Value("${spring.mail.username}")
    private String botEmail;

    @Value("${ultimatechatapp.app.url}")
    private String baseUrl;


    public EmailService(JavaMailSender javaMailSender, SpringTemplateEngine templateEngine) {
        this.javaMailSender = javaMailSender;
        this.templateEngine = templateEngine;
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
            log.error("Error sending email to {}", Arrays.toString(recipient));
            return false;
        }

        log.info("Email sent to {}", Arrays.toString(recipient));

        return true;
    }

    public boolean sendHtmlEmail(String subject, String htmlContent, String... recipient) {
        MimeMessage message = javaMailSender.createMimeMessage();

        try {
            MimeMessageHelper messageHelper = new MimeMessageHelper(message, true);

            messageHelper.setFrom(botEmail);
            messageHelper.setTo(recipient);
            messageHelper.setSubject(subject);

            messageHelper.setText(htmlContent, true);
        } catch (MessagingException e) {
            log.error("Error sending email to {}", Arrays.toString(recipient));
            e.printStackTrace();
            return false;
        }

        try {
            javaMailSender.send(message);
        } catch (MailException e) {
            log.error("Error sending email to {}", Arrays.toString(recipient));
            e.printStackTrace();
            return false;
        }

        log.info("Email sent to {}", Arrays.toString(recipient));

        return true;
    }

    public boolean sendVerificationEmail(User user, String verificationCode) {
        Context context = new Context();

        context.setVariable("userFullName", user.getUsername());
        context.setVariable("verificationCode", verificationCode);
        context.setVariable("verificationLink", baseUrl + "/verify/" + verificationCode);
        context.setVariable("contactEmail", botEmail);

        String html = templateEngine.process("verification-email.html", context);

        log.info("Built HTML template, Sending verification email to {}", user.getEmail());

        return sendHtmlEmail("[Ultimate Chat App] Verify your email address", html, user.getEmail());
    }

}
