package com.ds2025.deviceservice.rabbitmq;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Component
public class DeviceEventPublisher {

    private final RabbitTemplate rabbitTemplate;
    private final ObjectMapper objectMapper;

    public DeviceEventPublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
        this.objectMapper = new ObjectMapper();
    }

    public void publishDeviceCreated(UUID deviceId) {
        try {
            Map<String, Object> event = new HashMap<>();
            event.put("eventType", "DEVICE_CREATED");
            event.put("deviceId", deviceId.toString());

            String json = objectMapper.writeValueAsString(event);
            
            rabbitTemplate.convertAndSend("sync.exchange", "", json);            
        } 
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void publishDeviceDeleted(UUID deviceId) {
        try {
            Map<String, Object> event = new HashMap<>();
            event.put("eventType", "DEVICE_DELETED");
            event.put("deviceId", deviceId.toString());

            String json = objectMapper.writeValueAsString(event);
            
            rabbitTemplate.convertAndSend("sync.exchange", "", json);            
        } 
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void publishDeviceAssigned(UUID deviceId, UUID userId) {
        try {
            Map<String, Object> event = new HashMap<>();
            event.put("eventType", "DEVICE_ASSIGNED");
            event.put("deviceId", deviceId.toString());
            event.put("userId", userId.toString());

            String json = objectMapper.writeValueAsString(event);
            rabbitTemplate.convertAndSend("sync.exchange", "", json);            
        } 
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
