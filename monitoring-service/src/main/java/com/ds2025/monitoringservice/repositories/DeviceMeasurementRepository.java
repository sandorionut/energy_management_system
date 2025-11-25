package com.ds2025.monitoringservice.repositories;

import com.ds2025.monitoringservice.entities.DeviceMeasurement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface DeviceMeasurementRepository extends JpaRepository<DeviceMeasurement, UUID> {
    void deleteByDeviceId(UUID deviceId);
}
