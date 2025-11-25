package com.ds2025.deviceservice.dtos;

import lombok.*;

import java.util.UUID;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DeviceResponseDTO {
    private UUID id;
    private String name;
    private Double maxConsumption;
    private UUID userId;
}
