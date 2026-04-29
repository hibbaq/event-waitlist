package com.hibbaq.event_waitlist.repository;

import com.hibbaq.event_waitlist.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
}