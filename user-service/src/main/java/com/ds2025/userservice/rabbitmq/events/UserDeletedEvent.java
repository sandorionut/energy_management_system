package com.ds2025.userservice.rabbitmq.events;

import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class UserDeletedEvent {
    private String eventType = "USER_DELETED";
    private UUID userId;
    private Instant timestamp;
}
