package com.example.travelapp.services;

import com.example.travelapp.dto.LoginRequest;
import com.example.travelapp.dto.RegisterRequest;
import com.example.travelapp.models.User;
import com.example.travelapp.repository.UserRepository;

import com.example.travelapp.security.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

@Service
public class AuthService {
    @Autowired
    private OtpService otpService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JWTUtil jwtTokenUtil;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public String login(LoginRequest loginRequest) {
        // Validate the login credentials (username and password)
        if (isValidUser(loginRequest.getUsername(), loginRequest.getPassword())) {
            if(userRepository.findByUsername(loginRequest.getUsername()).isEmpty()) {
                throw new IllegalArgumentException("User not found");
            }
            User user = userRepository.findByUsername(loginRequest.getUsername()).get();
            if (!user.isVerified()) {
                throw new IllegalArgumentException("Account not verified. Please verify your email first.");
            }
            // Generate and return a token (e.g., JWT)
            return jwtTokenUtil.generateToken(loginRequest.getUsername());
        } else {
            // Throw an exception for invalid login
            throw new IllegalArgumentException("Invalid username or password");
        }
    }

    public String register(RegisterRequest registerRequest) {
        // Validate if username is already taken
        if (userRepository.findByUsername(registerRequest.getUsername()).isPresent()) {
            throw new IllegalArgumentException("Username is already taken");
        }

        // Check if the email already exists
        if (userRepository.findByEmail(registerRequest.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email is already taken");
        }

        // Create a new user
        User user = new User();
        user.setUsername(registerRequest.getUsername());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.setEmail(registerRequest.getEmail());
        user.setRole("USER");
        user.setVerified(false); // Set initial verification status

        // Generate and set OTP
        String otp = otpService.generateOTP();
        user.setOtp(otp);

        // Save user to database
        userRepository.save(user);

        // Send OTP email
        otpService.sendRegistrationOtp(user.getUsername(), user.getEmail(), otp);

        // Generate and return JWT token
        return jwtTokenUtil.generateToken(registerRequest.getUsername());
    }

    public boolean verifyOtp(String email, String otp) {
        Optional<User> userOptional = userRepository.findByEmail(email);

        if (userOptional.isEmpty()) {
            throw new IllegalArgumentException("User not found");
        }

        User user = userOptional.get();

        if (user.getOtp().equals(otp)) {
            user.setVerified(true);
            user.setOtp(null); // Clear the OTP after successful verification
            userRepository.save(user);
            return true;
        }

        return false;
    }

    private boolean isValidUser(String username, String password) {
        // Validate the user by querying the database
        Optional<User> userOptional = userRepository.findByUsername(username);

        if (userOptional.isEmpty()) {
            throw new IllegalArgumentException("User not found");
        }

        User user = userOptional.get();

        // Check if user is verified
        if (!user.isVerified()) {
            throw new IllegalArgumentException("Account not verified. Please verify your email first.");
        }

        return passwordEncoder.matches(password, user.getPassword());
    }

    public void resendOtp(String email) {
        Optional<User> userOptional = userRepository.findByEmail(email);

        if (userOptional.isEmpty()) {
            throw new IllegalArgumentException("User not found");
        }

        User user = userOptional.get();

        // Generate new OTP
        String newOtp = otpService.generateOTP();
        user.setOtp(newOtp);
        userRepository.save(user);

        // Send new OTP email
        otpService.sendRegistrationOtp(user.getUsername(), user.getEmail(), newOtp);
    }


    public String getLoggedInEmail() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof UserDetails) {
            return ((UserDetails) principal).getUsername();
        } else {
            return principal.toString(); // If it's a simple username (e.g., a JWT token)
        }
    }

}