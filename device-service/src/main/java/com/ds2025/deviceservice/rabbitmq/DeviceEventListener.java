package com.ds2025.deviceservice.rabbitmq;

import com.ds2025.deviceservice.entities.DeviceUser;
import com.ds2025.deviceservice.repositories.DeviceUserRepository;
import com.ds2025.deviceservice.services.DeviceService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class DeviceEventListener {

    private final DeviceUserRepository userRepo;
    private final DeviceService deviceService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public DeviceEventListener(DeviceUserRepository userRepo, DeviceService deviceService) {
        this.userRepo = userRepo;
        this.deviceService = deviceService;
    }

    @RabbitListener(queues = "sync.device", containerFactory = "simpleFactory")
    @SuppressWarnings("null")
    public void handleSyncEvent(String payload) {
        try {
            JsonNode root = objectMapper.readTree(payload);
            String eventType = root.path("eventType").asText(null);
            
            if ("USER_REGISTERED".equals(eventType)) {
                UUID userId = UUID.fromString(root.get("userId").asText());
                if (!userRepo.existsById(userId)) {
                    userRepo.save(new DeviceUser(userId));
                }
            } 
            else if ("USER_DELETED".equals(eventType)) {
                UUID userId = UUID.fromString(root.get("userId").asText());
                deviceService.deleteMappingsByUser(userId);
            }
        } 
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
