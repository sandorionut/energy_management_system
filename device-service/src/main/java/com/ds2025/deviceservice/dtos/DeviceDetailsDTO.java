package com.ds2025.deviceservice.dtos;


import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class DeviceDetailsDTO {
    private String name;
    private Double maxConsumption;
}
