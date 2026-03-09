package com.divyansh.portfolio.service;

import com.divyansh.portfolio.model.Contact;

import jakarta.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private static final Logger logger =
            LoggerFactory.getLogger(EmailService.class);

    private final JavaMailSender mailSender;

    @Value("${portfolio.admin.email}")
    private String adminEmail;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Async
    public void sendContactEmail(Contact contact) {

        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper =
                    new MimeMessageHelper(mimeMessage, true, "UTF-8");

            helper.setTo(adminEmail);
            helper.setSubject("📩 New Portfolio Contact Message");

            String htmlContent = buildHtmlTemplate(contact);

            helper.setText(htmlContent, true); // true = HTML

            mailSender.send(mimeMessage);

            logger.info("Async email sent successfully for contact ID: {}",
                    contact.getId());

        } catch (Exception e) {
            logger.error("Async email sending failed for contact ID: {}",
                    contact.getId(), e);
        }
    }

    private String buildHtmlTemplate(Contact contact) {

        return """
                <html>
                    <body style="font-family: Arial, sans-serif; background-color:#f4f4f4; padding:20px;">
                        <div style="max-width:600px; margin:auto; background:white; padding:20px; border-radius:8px;">
                            <h2 style="color:#4F46E5;">New Portfolio Message</h2>
                            <hr/>
                            <p><strong>Name:</strong> %s</p>
                            <p><strong>Email:</strong> %s</p>
                            <p><strong>Message:</strong></p>
                            <div style="background:#f9fafb; padding:10px; border-radius:5px;">
                                %s
                            </div>
                            <hr/>
                            <p style="font-size:12px; color:gray;">
                                This message was sent from your portfolio website.
                            </p>
                        </div>
                    </body>
                </html>
                """.formatted(
                contact.getName(),
                contact.getEmail(),
                contact.getMessage()
        );
    }
}