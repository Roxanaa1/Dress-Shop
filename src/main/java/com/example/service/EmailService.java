package com.example.service;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StreamUtils;
import java.io.IOException;
import org.springframework.mail.MailParseException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import java.nio.charset.StandardCharsets;
@Service
public class EmailService
{
        @Autowired
        private JavaMailSender mailSender;

        public void sendOrderConfirmationEmail(String to, String subject, String orderId) {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message);

            try {

                //citeste html ul
                Resource resource = new ClassPathResource("email.html");
                String htmlContent = StreamUtils.copyToString(resource.getInputStream(), StandardCharsets.UTF_8);

                htmlContent = htmlContent.replace("{{orderId}}", orderId);

                helper.setTo(to);
                helper.setSubject(subject);
                helper.setText(htmlContent, true);
            } catch (MessagingException | IOException e) {
                throw new MailParseException(e);
            }

            mailSender.send(message);
        }
    }
