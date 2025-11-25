package com.ds2025.userservice.services;

import com.ds2025.userservice.dtos.UserUpdateDTO;
import com.ds2025.userservice.dtos.UserViewAllDTO;
import com.ds2025.userservice.entities.User;
import com.ds2025.userservice.handlers.exceptions.ResourceNotFoundException;
import com.ds2025.userservice.rabbitmq.UserEventPublisher;
import com.ds2025.userservice.repositories.UserRepository;

import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final UserEventPublisher userEventPublisher;

    public UserService(UserRepository userRepository, UserEventPublisher userEventPublisher) {
        this.userEventPublisher = userEventPublisher;
        this.userRepository = userRepository;
    }

    @Transactional
    @SuppressWarnings("null")
    public UserViewAllDTO updateUser(@NonNull UUID id, UserUpdateDTO updateDTO) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));

        if (updateDTO.getFirstName() != null && !updateDTO.getFirstName().isBlank()) {
            user.setFirstName(updateDTO.getFirstName());
        }

        if (updateDTO.getLastName() != null && !updateDTO.getLastName().isBlank()) {
            user.setLastName(updateDTO.getLastName());
        }

        if ((updateDTO.getEmail() != null && !updateDTO.getEmail().isBlank()) ||
                (updateDTO.getPassword() != null && !updateDTO.getPassword().isBlank())) {

            userEventPublisher.publishUserUpdatedEvent(
                    user.getId(),
                    updateDTO.getEmail(),
                    updateDTO.getPassword()
            );

            if (updateDTO.getEmail() != null && !updateDTO.getEmail().isBlank()) {
                user.setEmail(updateDTO.getEmail());
            }
        }

        userRepository.save(user);
        return getUserViewByIdDTO(user);
    }

    @Transactional
    @SuppressWarnings("null")
    public void deleteUser(@NonNull UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        userEventPublisher.publishUserDeletedEvent(userId);
        userRepository.delete(user);
    }

    @Transactional(readOnly = true)
    public UserViewAllDTO getUserById(@NonNull UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        return getUserViewByIdDTO(user);
    }

    private UserViewAllDTO getUserViewByIdDTO(User user) {
        UserViewAllDTO dto = new UserViewAllDTO();
        dto.setId(user.getId());
        dto.setEmail(user.getEmail());
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        return dto;
    }

    @Transactional(readOnly = true)
    public List<UserViewAllDTO> getAllUsers() {
        return userRepository.findAll().stream()
                .map(user -> {
                    UserViewAllDTO dto = new UserViewAllDTO();
                    dto.setId(user.getId());
                    dto.setEmail(user.getEmail());
                    dto.setFirstName(user.getFirstName());
                    dto.setLastName(user.getLastName());
                    return dto;
                })
                .collect(Collectors.toList());
    }

    public boolean existsById(@NonNull UUID id) {
        return userRepository.existsById(id);
    }
}
