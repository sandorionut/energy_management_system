package com.ds2025.monitoringservice.controllers;

import com.ds2025.monitoringservice.dtos.EnergyConsumptionResponseDTO;
import com.ds2025.monitoringservice.dtos.HourlyEnergyConsumptionDTO;
import com.ds2025.monitoringservice.repositories.DeviceOwnershipRepository;
import com.ds2025.monitoringservice.services.MonitoringService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/monitoring")
public class MonitoringController {

    private final MonitoringService monitoringService;
    private final DeviceOwnershipRepository ownershipRepository;

    public MonitoringController(MonitoringService monitoringService, DeviceOwnershipRepository ownershipRepository) {
        this.monitoringService = monitoringService;
        this.ownershipRepository = ownershipRepository;
    }

    @GetMapping("/devices/{deviceId}/consumption")
    public ResponseEntity<EnergyConsumptionResponseDTO> getHourlyConsumption(
            @PathVariable UUID deviceId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            Authentication authentication) {
        UUID userId = UUID.fromString(authentication.getName());
        boolean isAdmin = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch(role -> role.equals("ROLE_ADMIN"));

        if (!isAdmin) {
            boolean ownsDevice = ownershipRepository.findByDeviceIdAndUserId(deviceId, userId).isPresent();
            if (!ownsDevice) {
                throw new ResponseStatusException(
                        HttpStatus.FORBIDDEN,
                        "You do not have permission to view this device's consumption data"
                );
            }
        }

        List<HourlyEnergyConsumptionDTO> hourlyData = monitoringService.getHourlyConsumption(deviceId, date);

        EnergyConsumptionResponseDTO response = new EnergyConsumptionResponseDTO();
        response.setDeviceId(deviceId);
        response.setDate(date);
        response.setHourlyData(hourlyData);

        return ResponseEntity.ok(response);
    }
}
