package com.example.travelapp.repository;

import com.example.travelapp.models.Booking;
import com.example.travelapp.models.User;
import com.example.travelapp.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
    User findByEmail(String email);
}
