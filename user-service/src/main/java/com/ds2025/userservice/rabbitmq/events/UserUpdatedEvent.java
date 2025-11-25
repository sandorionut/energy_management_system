package com.ds2025.userservice.rabbitmq.events;

import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class UserUpdatedEvent {
    private String eventType = "USER_UPDATED";
    private UUID userId;
    private String email;      
    private String password;   
    private Instant timestamp;
}
