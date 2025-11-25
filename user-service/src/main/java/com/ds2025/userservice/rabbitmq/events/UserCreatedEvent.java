package com.ds2025.userservice.rabbitmq.events;

import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class UserCreatedEvent {
    private String eventType = "USER_CREATED";
    private UUID userId;
    private String email;
    private Instant createdAt;
}
