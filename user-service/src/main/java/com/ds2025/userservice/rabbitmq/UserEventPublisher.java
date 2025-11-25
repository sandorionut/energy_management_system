package com.ds2025.userservice.rabbitmq;

import com.ds2025.userservice.rabbitmq.events.UserDeletedEvent;
import com.ds2025.userservice.rabbitmq.events.UserUpdatedEvent;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
public class UserEventPublisher {

    private final RabbitTemplate producerRabbitTemplate;

    public UserEventPublisher(RabbitTemplate template) {
        this.producerRabbitTemplate = template;
    }

    public void publishUserUpdatedEvent(UUID userId, String email, String password) {
        UserUpdatedEvent event = new UserUpdatedEvent(
                "USER_UPDATED",
                userId,
                email,
                password,
                Instant.now()
        );
        producerRabbitTemplate.convertAndSend("sync.exchange", "", event);
    }

    public void publishUserDeletedEvent(UUID userId) {
        UserDeletedEvent event = new UserDeletedEvent(
                "USER_DELETED",
                userId,
                Instant.now()
        );
        producerRabbitTemplate.convertAndSend("sync.exchange", "", event);
    }
}

