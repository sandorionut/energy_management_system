package com.ds2025.userservice.rabbitmq;

import com.ds2025.userservice.entities.User;
import com.ds2025.userservice.repositories.UserRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class UserEventListener {

    private final UserRepository userRepository;

    public UserEventListener(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @SuppressWarnings("null")
    @RabbitListener(queues = "sync.user", containerFactory = "simpleFactory")
    public void handleSyncEvent(String payload) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(payload);
            String eventType = root.path("eventType").asText();

            if ("USER_REGISTERED".equals(eventType)) {
                UUID userId = UUID.fromString(root.get("userId").asText());
                String email = root.get("email").asText();
                String firstName = root.get("firstName").asText();
                String lastName = root.get("lastName").asText();

                if (!userRepository.existsById(userId)) {
                    User u = new User();
                    u.setId(userId);
                    u.setEmail(email);
                    u.setFirstName(firstName);
                    u.setLastName(lastName);
                    userRepository.save(u);
                }
            }
        } 
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
