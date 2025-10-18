package com.HotelBookingSystem.BookingBro.service;

import com.HotelBookingSystem.BookingBro.model.Role;
import com.HotelBookingSystem.BookingBro.model.User;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface IUserService {

    // Get all users
    List<User> getAllUsers();

    // Get user by ID
    Optional<User> getUserById(Long id);

    // Update user details
    Optional<User> updateUser(Long id, User updatedUser);

    // Delete user
    boolean deleteUser(Long id);

    // Get user by email
    User getUserByEmail(String email);

    // Assign roles to user
    User assignRolesToUser(String email, Set<Role> roles);

    // Register new user with firstName, lastName, email, password, roles
    User registerUser(String firstName, String lastName, String email, String password, Set<Role> roles);

    // Login user with email
    boolean loginUser(String email, String password);
}
