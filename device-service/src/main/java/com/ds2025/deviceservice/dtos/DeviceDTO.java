package com.ds2025.deviceservice.dtos;

import lombok.*;

import java.util.UUID;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DeviceDTO {
    private UUID id;
    private String name;
    private Double maxConsumption;
}
