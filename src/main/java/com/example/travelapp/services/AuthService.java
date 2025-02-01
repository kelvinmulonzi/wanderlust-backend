package com.example.travelapp.services;

import com.example.travelapp.dto.LoginRequest;
import com.example.travelapp.dto.RegisterRequest;
import com.example.travelapp.models.User;
import com.example.travelapp.repository.UserRepository;
import com.example.travelapp.Security.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {
    @Autowired
    private OtpService otpService;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public String login(LoginRequest loginRequest) {
        // Validate the login credentials (username and password)
        if (isValidUser(loginRequest.getUsername(), loginRequest.getPassword())) {
            // Generate and return a token (e.g., JWT)
            return jwtTokenUtil.generateJWT(loginRequest.getUsername());
        } else {
            // Throw an exception for invalid login
            throw new IllegalArgumentException("Invalid username or password");
        }
    }

    public String register(RegisterRequest registerRequest) {
        if (userRepository.findByUsername(registerRequest.getUsername()).isPresent()) {
            throw new IllegalArgumentException("Username is already taken");
        }

        // Check if the email already exists
        if (userRepository.findByEmail(registerRequest.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email is already taken");
        }

        // Create a new user and save it to the database
        User user = new User();
        user.setUsername(registerRequest.getUsername());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.setEmail(registerRequest.getEmail());
        user.setRole("USER");
        String otp = otpService.generateOTP();
        user.setOtp(otp);
        userRepository.save(user);
        String subject = "Password Reset Request";
        String to = user.getEmail();
        // Password reset link (usually contains a token)
        String resetLink = "http://192.168.254.100:3000/passwordreset/?token=";
        String message = "<!DOCTYPE html>\n" +
                "<html>\n" +
                "<head>\n" +
                "    <title>Password Reset Request</title>\n" +
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
                "            <p>Hello {{name}},</p>\n" +
                "            <p>We received a request to register an account with your email.</p>\n" +
                "            <p>Use the OTP below:</p>\n" +
                "            <h2>{{otp}}</h2>\n" +
                "            <p>If you did not register an account,ignore this email.</p>\n" +
                "            <p>Thank you!</p>\n" +
                "        </div>\n" +
                "        <div class=\"footer\">\n" +
                "            <p>If you have any questions, please contact us at support@mulonzikelvin.com</p>\n" +
                "        </div>\n" +
                "    </div>\n" +
                "</body>\n" +
                "</html>";
        message = message.replace("{{otp}}", otp);
       message = message.replace("{{name}}", user.getUsername());
        otpService.sendEmail(to, subject, message);
        // Generate and return a token (e.g., JWT)
        return jwtTokenUtil.generateJWT(registerRequest.getUsername());
    }

    private boolean isValidUser(String username, String password) {
        // Validate the user by querying the database
        Optional<User> userOptional = userRepository.findByUsername(username);

        if (userOptional.isEmpty()) {
            throw new IllegalArgumentException("User not found");
        }

        User user = userOptional.get();
        return passwordEncoder.matches(password, user.getPassword());
    }
}