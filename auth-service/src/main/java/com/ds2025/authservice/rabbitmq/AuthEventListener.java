package com.ds2025.authservice.rabbitmq;

import com.ds2025.authservice.services.AuthService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class AuthEventListener {

    private final AuthService authService;

    public AuthEventListener(AuthService authService) {
        this.authService = authService;
    }

    @RabbitListener(queues = "sync.auth", containerFactory = "simpleFactory")
    @SuppressWarnings("null")
    public void handleSyncEvent(String payload) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(payload);
            String eventType = root.path("eventType").asText();

            if ("USER_UPDATED".equals(eventType)) {
                UUID userId = UUID.fromString(root.get("userId").asText());
                String email = root.has("email") && !root.get("email").isNull() 
                        ? root.get("email").asText() : null;
                String password = root.has("password") && !root.get("password").isNull() 
                        ? root.get("password").asText() : null;

                authService.handleUserUpdated(userId, email, password);
            } 
            else if ("USER_DELETED".equals(eventType)) {
                UUID userId = UUID.fromString(root.get("userId").asText());

                authService.handleUserDeleted(userId);
            }
        } 
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
