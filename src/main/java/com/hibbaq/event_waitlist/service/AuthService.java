package com.hibbaq.event_waitlist.service;

import com.hibbaq.event_waitlist.model.User;
import com.hibbaq.event_waitlist.repository.UserRepository;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class AuthService {

    private final UserRepository userRepository;

    public AuthService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User register(String email, String name, String password, String role) {
        Optional<User> existing = userRepository.findByEmail(email);
        if (existing.isPresent()) {
            throw new RuntimeException("Email already registered");
        }
        
        User.Role userRole = "ORGANIZER".equalsIgnoreCase(role) ? User.Role.ORGANIZER : User.Role.USER;
        User user = new User(email, name, password, userRole);
        return userRepository.save(user);
    }

    public User login(String email, String password) {
        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("User not found"));
        
        if (!user.getPassword().equals(password)) {
            throw new RuntimeException("Invalid password");
        }
        
        return user;
    }
}
