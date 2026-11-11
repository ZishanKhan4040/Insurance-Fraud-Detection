
package com.motorinsurance.frauddetectionsystem.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.motorinsurance.frauddetectionsystem.dto.RegisterRequest;
import com.motorinsurance.frauddetectionsystem.entity.User;
import com.motorinsurance.frauddetectionsystem.repository.UserRepository;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public User register(RegisterRequest request) {

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException(
                    "Email is already registered");
        }

        User user = new User();
        user.setFullName(request.getFullName());
        user.setEmail(request.getEmail());
        user.setPasswordHash(
                passwordEncoder.encode(request.getPassword()));

        // Public registration must never grant investigator privileges.
        user.setRole(User.Role.CLAIMANT);

        return userRepository.save(user);
    }
    

@Transactional(readOnly = true)
public User login(
        com.motorinsurance.frauddetectionsystem.dto.LoginRequest request) {

    User user = userRepository.findByEmail(request.getEmail())
            .orElseThrow(() ->
                    new IllegalArgumentException(
                            "Invalid email or password"));

    if (!passwordEncoder.matches(
            request.getPassword(), user.getPasswordHash())) {
        throw new IllegalArgumentException(
                "Invalid email or password");
    }

    return user;
}

}
