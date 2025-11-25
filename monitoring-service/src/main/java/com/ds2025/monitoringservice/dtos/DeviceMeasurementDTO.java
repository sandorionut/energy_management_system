package com.ds2025.monitoringservice.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeviceMeasurementDTO {
    private LocalDateTime timestamp;
    private UUID deviceId;
    private Double measurementValue;
}
