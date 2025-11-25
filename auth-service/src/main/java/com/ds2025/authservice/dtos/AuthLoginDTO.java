package com.ds2025.authservice.dtos;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AuthLoginDTO {
    private String email;
    private String password;
}
