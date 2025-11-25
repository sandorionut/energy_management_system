package com.ds2025.deviceservice.controllers;

import com.ds2025.deviceservice.dtos.DeviceDTO;
import com.ds2025.deviceservice.dtos.DeviceResponseDTO;
import com.ds2025.deviceservice.dtos.DeviceDetailsDTO;
import com.ds2025.deviceservice.services.DeviceService;
import jakarta.validation.Valid;

import org.springframework.lang.NonNull;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/device")
public class DeviceController {

    private final DeviceService deviceService;

    public DeviceController(DeviceService deviceService) {
        this.deviceService = deviceService;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public DeviceDTO create(@Valid @RequestBody DeviceDetailsDTO req) {
        return deviceService.createDevice(req);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/{deviceId}/assign/{userId}")
    public void assignDeviceToUser(@NonNull @PathVariable UUID deviceId,
                                   @NonNull @PathVariable UUID userId) {
        deviceService.assignDeviceToUser(deviceId, userId);
    }

    @GetMapping
    public List<DeviceResponseDTO> getUserDevices(Authentication authentication) {
        UUID userId = UUID.fromString(authentication.getName());
        return deviceService.getDevicesByUser(userId);
    }

    @GetMapping("/{name}")
    public DeviceResponseDTO getDeviceByName(@PathVariable String name, Authentication authentication) {
        UUID userId = UUID.fromString(authentication.getName());
        return deviceService.getDeviceByName(name, userId);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public DeviceDTO updateDevice(@NonNull @PathVariable UUID id,
                                  @Valid @RequestBody DeviceDetailsDTO req) {
        return deviceService.updateDeviceAsAdmin(id, req);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public void deleteDevice(@NonNull @PathVariable UUID id) {
        deviceService.deleteDeviceAsAdmin(id);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/all")
    public List<DeviceDTO> getAllDevicesForAdmin() {
        return deviceService.getAllDevices();
    }
}


