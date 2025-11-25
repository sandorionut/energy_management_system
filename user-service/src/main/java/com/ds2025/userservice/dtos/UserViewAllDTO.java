package com.ds2025.userservice.dtos;

import lombok.*;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserViewAllDTO {
    private UUID id;
    private String email;
    private String firstName;
    private String lastName;
}
