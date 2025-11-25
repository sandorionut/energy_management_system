package com.ds2025.deviceservice.services;

import com.ds2025.deviceservice.dtos.DeviceDTO;
import com.ds2025.deviceservice.dtos.DeviceResponseDTO;
import com.ds2025.deviceservice.dtos.DeviceDetailsDTO;
import com.ds2025.deviceservice.entities.Device;
import com.ds2025.deviceservice.entities.DeviceUser;
import com.ds2025.deviceservice.entities.DeviceUserMapping;
import com.ds2025.deviceservice.rabbitmq.DeviceEventPublisher;
import com.ds2025.deviceservice.repositories.DeviceRepository;
import com.ds2025.deviceservice.repositories.DeviceUserMappingRepository;
import com.ds2025.deviceservice.repositories.DeviceUserRepository;

import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class DeviceService {
    private final DeviceRepository deviceRepository;
    private final DeviceUserMappingRepository mappingRepository;
    private final DeviceUserRepository userRepository;
    private final DeviceEventPublisher eventPublisher;

    public DeviceService(DeviceRepository deviceRepository,
                         DeviceUserMappingRepository mappingRepository,
                         DeviceUserRepository userRepository,
                         DeviceEventPublisher eventPublisher) {
        this.deviceRepository = deviceRepository;
        this.mappingRepository = mappingRepository;
        this.userRepository = userRepository;
        this.eventPublisher = eventPublisher;
    }

    @Transactional
    public DeviceDTO createDevice(DeviceDetailsDTO req) {
        Device device = new Device();
        device.setName(req.getName());
        device.setMaxConsumption(req.getMaxConsumption());
        Device saved = deviceRepository.save(device);
        
        eventPublisher.publishDeviceCreated(saved.getId());
        return new DeviceDTO(saved.getId(), saved.getName(), saved.getMaxConsumption());
    }

    @Transactional
    public void assignDeviceToUser(@NonNull UUID deviceId, @NonNull UUID userId) {
        if (!deviceRepository.existsById(deviceId)) {
            throw new IllegalArgumentException("Device not found: " + deviceId);
        }

        if (!checkUserExists(userId)) {
            throw new IllegalArgumentException("Invalid user ID: no user found with id " + userId);
        }

        boolean alreadyLinked = mappingRepository.findByDevice_IdAndUser_Id(deviceId, userId).isPresent();
        if (alreadyLinked) {
            throw new IllegalArgumentException("Device already assigned to this user");
        }

        Device device = deviceRepository.findById(deviceId)
                .orElseThrow(() -> new IllegalArgumentException("Device not found"));

        DeviceUser user = new DeviceUser();
        user.setId(userId);  

        DeviceUserMapping mapping = new DeviceUserMapping();
        mapping.setDevice(device);
        mapping.setUser(user);

        mappingRepository.save(mapping);        
        eventPublisher.publishDeviceAssigned(deviceId, userId);
    }

    private boolean checkUserExists(@NonNull UUID userId) {
        return userRepository.existsById(userId);
    }

    @Transactional(readOnly = true)
    @SuppressWarnings("null")
    public List<DeviceResponseDTO> getDevicesByUser(UUID userId) {
        var mappings = mappingRepository.findByUser_Id(userId);
        var deviceIds = mappings.stream()
                .map(m -> m.getDevice().getId())
                .toList();

        return deviceRepository.findAllById(deviceIds)
                .stream()
                .map(d -> new DeviceResponseDTO(d.getId(), d.getName(), d.getMaxConsumption(), userId))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    @SuppressWarnings("null")
    public DeviceResponseDTO getDeviceByName(String name, UUID userId) {
        var mappings = mappingRepository.findByUser_Id(userId);
        var deviceIds = mappings.stream()
                .map(m -> m.getDevice().getId())
                .toList();

        Device device = deviceRepository.findAllById(deviceIds)
                .stream()
                .filter(d -> d.getName().equalsIgnoreCase(name))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Device not found with name: " + name));

        return new DeviceResponseDTO(device.getId(), device.getName(), device.getMaxConsumption(), userId);
    }

    @Transactional
    @SuppressWarnings("null")
    public DeviceDTO updateDeviceAsAdmin(@NonNull UUID id, DeviceDetailsDTO req) {
        Device device = deviceRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Device not found"));

        if (req.getName() != null && !req.getName().isBlank())
            device.setName(req.getName());
        if (req.getMaxConsumption() != null)
            device.setMaxConsumption(req.getMaxConsumption());

        Device updated = deviceRepository.save(device);
        return new DeviceDTO(updated.getId(), updated.getName(), updated.getMaxConsumption());
    }

    @Transactional
    @SuppressWarnings("null")
    public void deleteDeviceAsAdmin(@NonNull UUID id) {
        if (!deviceRepository.existsById(id))
            throw new IllegalArgumentException("Device not found");

        mappingRepository.findByDevice_Id(id)
                .forEach(m -> mappingRepository.deleteById(m.getId()));

        deviceRepository.deleteById(id);        
        eventPublisher.publishDeviceDeleted(id);
    }

    @Transactional
    public void deleteMappingsByUser(@NonNull UUID userId) {
        mappingRepository.deleteAllByUserId(userId);
        userRepository.deleteById(userId);
    }

    public List<DeviceDTO> getAllDevices() {
        return deviceRepository.findAll()
                .stream()
                .map(d -> new DeviceDTO(d.getId(), d.getName(), d.getMaxConsumption()))
                .toList();
    }

}
