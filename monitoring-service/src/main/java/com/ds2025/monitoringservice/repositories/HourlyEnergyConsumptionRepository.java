package com.ds2025.monitoringservice.repositories;

import com.ds2025.monitoringservice.entities.HourlyEnergyConsumption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface HourlyEnergyConsumptionRepository extends JpaRepository<HourlyEnergyConsumption, UUID> {

    Optional<HourlyEnergyConsumption> findByDeviceIdAndDateAndHour(UUID deviceId, LocalDate date, Integer hour);

    void deleteByDeviceId(UUID deviceId);

    @Query("SELECT h FROM HourlyEnergyConsumption h WHERE h.deviceId = :deviceId AND h.date = :date ORDER BY h.hour")
    List<HourlyEnergyConsumption> findByDeviceIdAndDate(@Param("deviceId") UUID deviceId, @Param("date") LocalDate date);
}
