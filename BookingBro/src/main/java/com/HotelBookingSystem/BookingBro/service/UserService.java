package com.HotelBookingSystem.BookingBro.service;

import com.HotelBookingSystem.BookingBro.model.Role;
import com.HotelBookingSystem.BookingBro.model.User;
import com.HotelBookingSystem.BookingBro.repository.RoleRepository;
import com.HotelBookingSystem.BookingBro.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService implements IUserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // ------------------- LOGIN -------------------
    @Override
    public boolean loginUser(String email, String password) {
        Optional<User> userOpt = userRepository.findByEmail(email);
        if (userOpt.isEmpty()) return false;

        User user = userOpt.get();
        return passwordEncoder.matches(password, user.getPassword());
    }

    // ------------------- GET ALL USERS -------------------
    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // ------------------- GET USER BY ID -------------------
    @Override
    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    // ------------------- GET USER BY EMAIL -------------------
    @Override
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));
    }

    // ------------------- REGISTER USER -------------------
    @Override
    public User registerUser(String firstName, String lastName, String email, String password, Set<Role> roles) {
        if (userRepository.findByEmail(email).isPresent()) {
            throw new RuntimeException("Email already exists");
        }

        User user = new User();
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));

        // Fetch persisted roles from DB
        Set<Role> persistedRoles = roles.stream()
                .map(role -> roleRepository.findByName(role.getName())
                        .orElseThrow(() -> new RuntimeException("Role not found: " + role.getName())))
                .collect(Collectors.toSet());
        user.setRoles(persistedRoles);

        return userRepository.save(user);
    }

    // ------------------- UPDATE USER -------------------
    @Override
    public Optional<User> updateUser(Long id, User updatedUser) {
        return userRepository.findById(id)
                .map(user -> {
                    user.setFirstName(updatedUser.getFirstName());
                    user.setLastName(updatedUser.getLastName());
                    user.setEmail(updatedUser.getEmail());

                    // Update password only if provided
                    if (updatedUser.getPassword() != null && !updatedUser.getPassword().isBlank()) {
                        user.setPassword(passwordEncoder.encode(updatedUser.getPassword()));
                    }

                    // Update roles if provided
                    if (updatedUser.getRoles() != null && !updatedUser.getRoles().isEmpty()) {
                        Set<Role> persistedRoles = updatedUser.getRoles().stream()
                                .map(role -> roleRepository.findByName(role.getName())
                                        .orElseThrow(() -> new RuntimeException("Role not found: " + role.getName())))
                                .collect(Collectors.toSet());
                        user.setRoles(persistedRoles);
                    }

                    return userRepository.save(user);
                });
    }

    // ------------------- DELETE USER -------------------
    @Override
    public boolean deleteUser(Long id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
            return true;
        }
        return false;
    }

    // ------------------- ASSIGN ROLES TO USER -------------------
    @Override
    public User assignRolesToUser(String email, Set<Role> roles) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Set<Role> persistedRoles = roles.stream()
                .map(role -> roleRepository.findByName(role.getName())
                        .orElseThrow(() -> new RuntimeException("Role not found: " + role.getName())))
                .collect(Collectors.toSet());

        user.setRoles(persistedRoles);
        return userRepository.save(user);
    }
}
