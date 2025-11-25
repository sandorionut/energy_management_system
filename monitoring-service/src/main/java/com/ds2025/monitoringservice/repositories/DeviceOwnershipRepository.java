package com.ds2025.monitoringservice.repositories;

import com.ds2025.monitoringservice.entities.DeviceOwnership;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface DeviceOwnershipRepository extends JpaRepository<DeviceOwnership, UUID> {
    Optional<DeviceOwnership> findByDeviceIdAndUserId(UUID deviceId, UUID userId);
    
    List<DeviceOwnership> findByUserId(UUID userId);
    
    List<DeviceOwnership> findByDeviceId(UUID deviceId);
    
    void deleteByDeviceId(UUID deviceId);
    
    void deleteByUserId(UUID userId);
}
