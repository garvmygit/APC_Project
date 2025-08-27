package org.GG.apcProject.controller;

import org.GG.apcProject.model.User;
import org.GG.apcProject.repository.UserRepository;
import org.GG.apcProject.dto.LoginRequest;
import org.GG.apcProject.dto.SignupRequest;
import org.GG.apcProject.config.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private AuthenticationManager authManager;

    // ---------------- REGISTER ----------------
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody SignupRequest req) {
        // Check if username already exists
        if (userRepo.findByUsername(req.getUsername()).isPresent()) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "Username is already taken"));
        }

        // Create and save user
        User u = new User();
        u.setUsername(req.getUsername());
        u.setPassword(encoder.encode(req.getPassword()));
        u.setFullName(req.getFullName());
        u.setEmail(req.getEmail());
        u.setRoles(Set.of("ROLE_USER")); // default role
        userRepo.save(u);

        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                "message", "User registered successfully",
                "username", u.getUsername()
        ));
    }

    // ---------------- LOGIN ----------------
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest req) {
        try {
            // Authenticate user credentials
            authManager.authenticate(
                    new UsernamePasswordAuthenticationToken(req.getUsername(), req.getPassword())
            );

            // Load user from DB
            User u = userRepo.findByUsername(req.getUsername())
                    .orElseThrow(() -> new BadCredentialsException("Invalid username or password"));

            // Generate JWT token
            String token = jwtUtil.generateToken(u.getUsername());

            return ResponseEntity.ok(Map.of(
                    "token", token,
                    "username", u.getUsername(),
                    "roles", u.getRoles()
            ));

        } catch (BadCredentialsException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Invalid username or password"));
        }
    }
}
