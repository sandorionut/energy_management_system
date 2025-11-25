package com.ds2025.monitoringservice.services;

import com.ds2025.monitoringservice.dtos.DeviceMeasurementDTO;
import com.ds2025.monitoringservice.dtos.HourlyEnergyConsumptionDTO;
import com.ds2025.monitoringservice.entities.DeviceMeasurement;
import com.ds2025.monitoringservice.entities.HourlyEnergyConsumption;
import com.ds2025.monitoringservice.repositories.DeviceMeasurementRepository;
import com.ds2025.monitoringservice.repositories.HourlyEnergyConsumptionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class MonitoringService {
    private final DeviceMeasurementRepository measurementRepository;
    private final HourlyEnergyConsumptionRepository hourlyConsumptionRepository;

    public MonitoringService(DeviceMeasurementRepository measurementRepository,
                              HourlyEnergyConsumptionRepository hourlyConsumptionRepository) {
        this.measurementRepository = measurementRepository;
        this.hourlyConsumptionRepository = hourlyConsumptionRepository;
    }

    @Transactional
    public void processMeasurement(DeviceMeasurementDTO measurementDTO) {
        DeviceMeasurement measurement = new DeviceMeasurement();
        measurement.setDeviceId(measurementDTO.getDeviceId());
        measurement.setTimestamp(measurementDTO.getTimestamp());
        measurement.setMeasurementValue(measurementDTO.getMeasurementValue());
        measurementRepository.save(measurement);

        updateHourlyConsumption(measurementDTO);
    }

    private void updateHourlyConsumption(DeviceMeasurementDTO measurementDTO) {
        LocalDate date = measurementDTO.getTimestamp().toLocalDate();
        int hour = measurementDTO.getTimestamp().getHour();

        HourlyEnergyConsumption hourlyData = hourlyConsumptionRepository
                .findByDeviceIdAndDateAndHour(measurementDTO.getDeviceId(), date, hour)
                .orElse(new HourlyEnergyConsumption());

        if (hourlyData.getId() == null) {
            hourlyData.setDeviceId(measurementDTO.getDeviceId());
            hourlyData.setDate(date);
            hourlyData.setHour(hour);
            hourlyData.setTotalConsumption(measurementDTO.getMeasurementValue());
            hourlyData.setMeasurementCount(1);
        } 
        else {
            hourlyData.setTotalConsumption(hourlyData.getTotalConsumption() + measurementDTO.getMeasurementValue());
            hourlyData.setMeasurementCount(hourlyData.getMeasurementCount() + 1);
        }

        hourlyConsumptionRepository.save(hourlyData);
    }

    public List<HourlyEnergyConsumptionDTO> getHourlyConsumption(UUID deviceId, LocalDate date) {
        return hourlyConsumptionRepository.findByDeviceIdAndDate(deviceId, date)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private HourlyEnergyConsumptionDTO convertToDTO(HourlyEnergyConsumption entity) {
        return new HourlyEnergyConsumptionDTO(
                entity.getId(),
                entity.getDeviceId(),
                entity.getDate(),
                entity.getHour(),
                entity.getTotalConsumption(),
                entity.getMeasurementCount()
        );
    }
}
