package com.example.travelapp.services;

import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class OtpService {
    @Autowired
    private JavaMailSender emailSender;

    private static final String EMAIL_REGEX = "^(.+)@(.+)$";

    public static boolean isValidEmail(String email) {
        Pattern pattern = Pattern.compile(EMAIL_REGEX);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public String generateOTP() {
        Random random = new Random();
        int otp = 100000 + random.nextInt(900000);
        return String.valueOf(otp);
    }

    public void sendRegistrationOtp(String username, String email, String otp) {
        String subject = "Account Registration OTP";
        String emailContent = buildRegistrationEmailTemplate(username, otp);
        sendEmail(email, subject, emailContent);
    }

    private String buildRegistrationEmailTemplate(String username, String otp) {
        return "<!DOCTYPE html>\n" +
                "<html>\n" +
                "<head>\n" +
                "    <title>Account Registration</title>\n" +
                "    <style>\n" +
                "        body { font-family: Arial, sans-serif; }\n" +
                "        .container { max-width: 600px; margin: 0 auto; padding: 20px; }\n" +
                "        .header { background-color: #f4f4f4; padding: 10px; text-align: center; }\n" +
                "        .content { padding: 20px; background-color: #ffffff; border: 1px solid #ddd; }\n" +
                "        .footer { font-size: 0.9em; color: #888; text-align: center; padding: 10px; }\n" +
                "        .button { color:white;display: inline-block; padding: 10px 20px; font-size: 16px; color: #fff; background-color: #007bff; text-decoration: none; border-radius: 5px; }\n" +
                "    </style>\n" +
                "</head>\n" +
                "<body>\n" +
                "    <div class=\"container\">\n" +
                "        <div class=\"header\">\n" +
                "            <h1>Account Registration</h1>\n" +
                "        </div>\n" +
                "        <div class=\"content\">\n" +
                "            <p>Hello " + username + ",</p>\n" +
                "            <p>We received a request to register an account with your email.</p>\n" +
                "            <p>Use the OTP below:</p>\n" +
                "            <h2>" + otp + "</h2>\n" +
                "            <p>If you did not register an account, ignore this email.</p>\n" +
                "            <p>Thank you!</p>\n" +
                "        </div>\n" +
                "        <div class=\"footer\">\n" +
                "            <p>If you have any questions, please contact us at support@mulonzikelvin.com</p>\n" +
                "        </div>\n" +
                "    </div>\n" +
                "</body>\n" +
                "</html>";
    }

    public void sendEmail(String to, String subject, String text) {
        MimeMessage message = emailSender.createMimeMessage();

        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(text, true); // true indicates HTML content
            emailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send email", e);
        }
    }

    // Add method to verify OTP
    public boolean verifyOTP(String email, String otp) {
        // Here you would typically:
        // 1. Look up the stored OTP for this email
        // 2. Compare it with the provided OTP
        // 3. Check if the OTP is still valid (not expired)
        // For now, we'll just return true if the OTP matches what's stored
        // You should implement proper storage and verification logic
        return true; // Implement actual verification logic
    }
}