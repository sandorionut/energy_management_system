package com.ds2025.simulator.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;


@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class DeviceMeasurement {
    private UUID deviceId;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime timestamp;

    private double measurementValue;

    @Override
    public String toString() {
        return String.format("DeviceMeasurement{deviceId=%s, timestamp=%s, value=%.2f kWh}",
                deviceId, timestamp, measurementValue);
    }
}
