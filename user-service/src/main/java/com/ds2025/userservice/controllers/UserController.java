package com.ds2025.userservice.controllers;

import com.ds2025.userservice.dtos.UserUpdateDTO;
import com.ds2025.userservice.dtos.UserViewAllDTO;
import com.ds2025.userservice.services.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService s) {
        this.userService = s;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<UserViewAllDTO> updateUserAsAdmin(
            @NonNull @PathVariable UUID id,
            @Valid @RequestBody UserUpdateDTO updateDTO) {
        UserViewAllDTO updated = userService.updateUser(id, updateDTO);
        return ResponseEntity.ok(updated);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUserAsAdmin(@NonNull @PathVariable UUID id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<UserViewAllDTO> getUserById(@NonNull @PathVariable UUID id) {
        UserViewAllDTO user = userService.getUserById(id);
        return ResponseEntity.ok(user);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<UserViewAllDTO>> getAllUsers() {
        List<UserViewAllDTO> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }
}
