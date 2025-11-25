package com.ds2025.monitoringservice.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HourlyEnergyConsumptionDTO {
    private UUID id;
    private UUID deviceId;
    private LocalDate date;
    private Integer hour;
    private Double totalConsumption;
    private Integer measurementCount;
}
