package com.ds2025.deviceservice.repositories;

import com.ds2025.deviceservice.entities.DeviceUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface DeviceUserRepository extends JpaRepository<DeviceUser, UUID> {
}

