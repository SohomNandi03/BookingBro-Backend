package com.HotelBookingSystem.BookingBro.controller;

import com.HotelBookingSystem.BookingBro.model.Role;
import com.HotelBookingSystem.BookingBro.model.User;
import com.HotelBookingSystem.BookingBro.repository.RoleRepository;
import com.HotelBookingSystem.BookingBro.request.LoginRequest;
import com.HotelBookingSystem.BookingBro.request.RegisterRequest;
import com.HotelBookingSystem.BookingBro.response.JwtResponse;
import com.HotelBookingSystem.BookingBro.security.jwt.JwtUtils;
import com.HotelBookingSystem.BookingBro.service.IUserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final IUserService userService;
    private final RoleRepository roleRepository;
    private final JwtUtils jwtUtils;

    public AuthController(IUserService userService,
                          RoleRepository roleRepository,
                          JwtUtils jwtUtils) {
        this.userService = userService;
        this.roleRepository = roleRepository;
        this.jwtUtils = jwtUtils;
    }

    // -------------------- REGISTER NEW USER --------------------
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody RegisterRequest request) {
        try {
            // Check if role USER exists, else create
            Role userRole = roleRepository.findByName("USER")
                    .orElseGet(() -> roleRepository.save(new Role("USER")));

            Set<Role> roles = Set.of(userRole);

            User user = userService.registerUser(
                    request.getFirstName(),
                    request.getLastName(),
                    request.getEmail(),
                    request.getPassword(),
                    roles
            );

            return ResponseEntity.ok("User registered successfully with email: " + user.getEmail());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Registration failed: " + e.getMessage());
        }
    }

    // -------------------- LOGIN USER --------------------
    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest) {
        try {
            // Check if user exists
            User user = userService.getUserByEmail(loginRequest.getEmail());
            if (user == null || !userService.loginUser(loginRequest.getEmail(), loginRequest.getPassword())) {
                return ResponseEntity.status(401).body("Invalid email or password");
            }

            // Generate JWT token
            String token = jwtUtils.generateJwtToken(user);

            // Extract roles
            List<String> roles = user.getRoles().stream()
                    .map(Role::getName)
                    .toList();

            // Return JWT response
            JwtResponse jwtResponse = new JwtResponse(user.getId(), user.getEmail(), token, roles);
            return ResponseEntity.ok(jwtResponse);

        } catch (Exception e) {
            return ResponseEntity.status(500).body("An unexpected error occurred: " + e.getMessage());
        }
    }
}
