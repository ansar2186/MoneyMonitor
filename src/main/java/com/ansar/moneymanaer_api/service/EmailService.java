package com.ansar.moneymanaer_api.service;

import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {

    private final JavaMailSender mailSender;

   // @Value("${spring.mail.properties.mail.smtp.from}")
   // private String fromEmail;

    public void sendEmail(String to, String subject, String body) {
        try {

            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setFrom("Money Manager Team <ahmad.ansar74@gmail.com>");
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(body, true);  // TRUE = HTML enable
            mailSender.send(message);

            log.info("Email successfully sent to email {}", to);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
