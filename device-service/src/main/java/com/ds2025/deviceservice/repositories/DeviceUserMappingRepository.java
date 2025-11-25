package com.ds2025.deviceservice.repositories;

import com.ds2025.deviceservice.entities.DeviceUserMapping;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface DeviceUserMappingRepository extends JpaRepository<DeviceUserMapping, UUID> {
    List<DeviceUserMapping> findByUser_Id(UUID userId);
    
    List<DeviceUserMapping> findByDevice_Id(UUID deviceId);

    Optional<DeviceUserMapping> findByDevice_IdAndUser_Id(UUID deviceId, UUID userId);

    void deleteAllByUserId(UUID userId);
}
