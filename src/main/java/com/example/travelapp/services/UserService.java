package com.example.travelapp.services;

import com.example.travelapp.models.User;
import com.example.travelapp.repository.UserRepository;
import lombok.Data;
import org.springframework.stereotype.Service;

import java.util.List;

@Data
@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User createUser(User user) {
        return userRepository.save(user);
    }

    public User getUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User updateUser(Long userId, User user) {
        User existingUser = getUser(userId);
        existingUser.setRole(user.getRole());
        existingUser.setEmail(user.getEmail());
        existingUser.setUsername(user.getUsername());
        // Update other fields as necessary
        return userRepository.save(existingUser);
    }

    public void deleteUser(Long userId) {
        User user = getUser(userId);
        userRepository.delete(user);
    }
}