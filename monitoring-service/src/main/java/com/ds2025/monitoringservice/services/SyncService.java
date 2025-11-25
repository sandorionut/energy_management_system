package com.ds2025.monitoringservice.services;

import com.ds2025.monitoringservice.entities.Device;
import com.ds2025.monitoringservice.entities.DeviceOwnership;
import com.ds2025.monitoringservice.repositories.DeviceOwnershipRepository;
import com.ds2025.monitoringservice.repositories.DeviceRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class SyncService {
    private final DeviceRepository deviceRepository;
    private final DeviceOwnershipRepository ownershipRepository;
    private final com.ds2025.monitoringservice.repositories.DeviceMeasurementRepository measurementRepository;
    private final com.ds2025.monitoringservice.repositories.HourlyEnergyConsumptionRepository consumptionRepository;
    private final ObjectMapper objectMapper;

    public SyncService(DeviceRepository deviceRepository, 
                       DeviceOwnershipRepository ownershipRepository,
                       com.ds2025.monitoringservice.repositories.DeviceMeasurementRepository measurementRepository,
                       com.ds2025.monitoringservice.repositories.HourlyEnergyConsumptionRepository consumptionRepository) {
        this.deviceRepository = deviceRepository;
        this.ownershipRepository = ownershipRepository;
        this.measurementRepository = measurementRepository;
        this.consumptionRepository = consumptionRepository;
        this.objectMapper = new ObjectMapper();
    }

    @Transactional
    public void handleDeviceCreated(String message) {
        try {
            JsonNode jsonNode = objectMapper.readTree(message);
            UUID deviceId = UUID.fromString(jsonNode.get("deviceId").asText());

            Device device = new Device();
            device.setId(deviceId);

            deviceRepository.save(device);
        } 
        catch (Exception e) {
        }
    }

    @Transactional
    public void handleDeviceAssigned(String message) {
        try {
            JsonNode jsonNode = objectMapper.readTree(message);
            UUID deviceId = UUID.fromString(jsonNode.get("deviceId").asText());
            UUID userId = UUID.fromString(jsonNode.get("userId").asText());

            DeviceOwnership ownership = new DeviceOwnership();
            ownership.setDeviceId(deviceId);
            ownership.setUserId(userId);

            ownershipRepository.save(ownership);
        } 
        catch (Exception e) {
        }
    }

    @Transactional
    @SuppressWarnings("null")
    public void handleDeviceDeleted(String message) {
        try {
            JsonNode jsonNode = objectMapper.readTree(message);
            UUID deviceId = UUID.fromString(jsonNode.get("deviceId").asText());

            ownershipRepository.deleteByDeviceId(deviceId);
            measurementRepository.deleteByDeviceId(deviceId);
            consumptionRepository.deleteByDeviceId(deviceId);
            deviceRepository.deleteById(deviceId);
        } 
        catch (Exception e) {
        }
    }

    @Transactional
    public void handleUserDeleted(String message) {
        try {
            JsonNode jsonNode = objectMapper.readTree(message);
            UUID userId = UUID.fromString(jsonNode.get("userId").asText());

            ownershipRepository.deleteByUserId(userId);
        } 
        catch (Exception e) {
        }
    }
}
