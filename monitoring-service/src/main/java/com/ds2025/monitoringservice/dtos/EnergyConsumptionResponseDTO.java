package com.ds2025.monitoringservice.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EnergyConsumptionResponseDTO {
    private UUID deviceId;
    private LocalDate date;
    private List<HourlyEnergyConsumptionDTO> hourlyData;
}
