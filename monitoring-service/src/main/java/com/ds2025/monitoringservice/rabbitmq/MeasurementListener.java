package com.ds2025.monitoringservice.rabbitmq;

import com.ds2025.monitoringservice.dtos.DeviceMeasurementDTO;
import com.ds2025.monitoringservice.services.MonitoringService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class MeasurementListener {
    private final MonitoringService monitoringService;
    private final ObjectMapper objectMapper;

    public MeasurementListener(MonitoringService monitoringService) {
        this.monitoringService = monitoringService;
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
    }

    @RabbitListener(queues = "device.measurements", containerFactory = "simpleFactory")
    public void handleMeasurement(String message) {
        try {
            DeviceMeasurementDTO measurement = objectMapper.readValue(message, DeviceMeasurementDTO.class);
            monitoringService.processMeasurement(measurement);
        } 
        catch (Exception e) {
        }
    }
}
