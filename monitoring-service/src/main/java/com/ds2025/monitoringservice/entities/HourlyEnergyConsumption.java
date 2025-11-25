package com.ds2025.monitoringservice.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "hourly_energy_consumption", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"device_id", "date", "hour"})
})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HourlyEnergyConsumption {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private UUID id;

    @Column(name = "device_id", nullable = false)
    private UUID deviceId;

    @Column(name = "date", nullable = false)
    private LocalDate date;

    @Column(name = "hour", nullable = false)
    private Integer hour;

    @Column(name = "total_consumption", nullable = false)
    private Double totalConsumption;

    @Column(name = "measurement_count", nullable = false)
    private Integer measurementCount;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
