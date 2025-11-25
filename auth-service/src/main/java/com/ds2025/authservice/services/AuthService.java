package com.ds2025.authservice.services;

import com.ds2025.authservice.dtos.AuthLoginDTO;
import com.ds2025.authservice.dtos.AuthRegisterDTO;
import com.ds2025.authservice.entities.AuthUser;
import com.ds2025.authservice.entities.Role;
import com.ds2025.authservice.rabbitmq.AuthEventPublisher;
import com.ds2025.authservice.repositories.AuthUserRepository;
import com.ds2025.authservice.repositories.RoleRepository;
import com.ds2025.authservice.security.JwtUtil;
import org.springframework.lang.NonNull;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
public class AuthService {
    private final AuthUserRepository authRepository;
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder encoder;
    private final JwtUtil jwtUtil;
    private final AuthEventPublisher authEventPublisher;


    public AuthService(AuthUserRepository authRepository,
                       RoleRepository roleRepository,
                       BCryptPasswordEncoder encoder,
                       JwtUtil jwtUtil,
                       AuthEventPublisher eventPublisher) {
        this.authRepository = authRepository;
        this.roleRepository = roleRepository;
        this.encoder = encoder;
        this.jwtUtil = jwtUtil;
        this.authEventPublisher = eventPublisher;
    }

    @Transactional
    public void register(AuthRegisterDTO req) {
        if (authRepository.findByEmail(req.getEmail()).isPresent()) {
            throw new IllegalArgumentException("email already in use");
        }

        AuthUser authUser = new AuthUser();
        authUser.setEmail(req.getEmail());
        authUser.setPasswordHash(encoder.encode(req.getPassword()));

        Role role = roleRepository.findByName("USER")
                .orElseThrow(() -> new RuntimeException("Default USER role missing"));
        authUser.setRole(role);

        authRepository.save(authUser);
        authEventPublisher.publishUserRegisteredEvent(authUser, req);
    }


    public String login(AuthLoginDTO req) {
        AuthUser authUser = authRepository.findByEmail(req.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("Invalid email or password"));

        if (!encoder.matches(req.getPassword(), authUser.getPasswordHash())) {
            throw new IllegalArgumentException("Invalid email or password");
        }

        String role = authUser.getRole().getName();
        if (role == null || role.isBlank()) {
            role = "USER";
        }

        return jwtUtil.generateToken(
                authUser.getEmail(),
                role,
                authUser.getId()
        );
    }

    @Transactional
    @SuppressWarnings("null")
    public void handleUserUpdated(@NonNull UUID userId, String email, String password) {
        AuthUser user = authRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Auth user not found"));

        if (email != null && !email.isBlank()) user.setEmail(email);
        if (password != null && !password.isBlank())
            user.setPasswordHash(encoder.encode(password));

        authRepository.save(user);
    }


    @Transactional
    @SuppressWarnings("null")
    public void handleUserDeleted(@NonNull UUID userId) {
        Optional<AuthUser> authUserOpt = authRepository.findById(userId);
        
        if (authUserOpt.isPresent()) {
            authRepository.delete(authUserOpt.get());
        }
    }

}
