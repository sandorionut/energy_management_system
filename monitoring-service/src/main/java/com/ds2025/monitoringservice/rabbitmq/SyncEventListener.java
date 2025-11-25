package com.ds2025.monitoringservice.rabbitmq;

import com.ds2025.monitoringservice.services.SyncService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class SyncEventListener {
    private final SyncService syncService;
    private final ObjectMapper objectMapper;

    public SyncEventListener(SyncService syncService) {
        this.syncService = syncService;
        this.objectMapper = new ObjectMapper();
    }

    @RabbitListener(queues = "sync.monitoring", containerFactory = "simpleFactory")
    public void handleSyncEvent(String message) {
        try {
            JsonNode jsonNode = objectMapper.readTree(message);
            String eventType = jsonNode.has("eventType") ? jsonNode.get("eventType").asText() : "";

            switch (eventType) {
                case "DEVICE_CREATED":
                    syncService.handleDeviceCreated(message);
                    break;
                case "DEVICE_ASSIGNED":
                    syncService.handleDeviceAssigned(message);
                    break;
                case "DEVICE_DELETED":
                    syncService.handleDeviceDeleted(message);
                    break;
                case "USER_DELETED":
                    syncService.handleUserDeleted(message);
                    break;
                default:
            }
        } 
        catch (Exception e) {
        }
    }
}
