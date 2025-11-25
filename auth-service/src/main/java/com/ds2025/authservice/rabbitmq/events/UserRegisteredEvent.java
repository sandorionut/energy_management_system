package com.ds2025.authservice.rabbitmq.events;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRegisteredEvent {
    private String eventType = "USER_REGISTERED";
    private UUID userId;
    private String email;
    private String firstName;
    private String lastName;
    private Instant createdAt;
}
