package com.HotelBookingSystem.BookingBro.repository;

import com.HotelBookingSystem.BookingBro.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);  // ✅ return Optional
    boolean existsByEmail(String email);
}
