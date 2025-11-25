package com.ds2025.authservice.dtos;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AuthRegisterDTO {
    private String firstName;
    private String lastName;
    private String email;
    private String password;
}
