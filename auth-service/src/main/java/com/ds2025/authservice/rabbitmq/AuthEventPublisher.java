package com.ds2025.authservice.rabbitmq;


import com.ds2025.authservice.entities.AuthUser;
import com.ds2025.authservice.dtos.AuthRegisterDTO;
import com.ds2025.authservice.rabbitmq.events.UserRegisteredEvent;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class AuthEventPublisher {

    private final RabbitTemplate rabbitTemplate;

    public AuthEventPublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void publishUserRegisteredEvent(AuthUser authUser, AuthRegisterDTO req) {
        UserRegisteredEvent event = new UserRegisteredEvent(
                "USER_REGISTERED",
                authUser.getId(),
                authUser.getEmail(),
                req.getFirstName(),
                req.getLastName(),
                Instant.now()
        );

        rabbitTemplate.convertAndSend("sync.exchange", "", event);
    }
}