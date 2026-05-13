package com.stretto.demo.features.internalUser.domain.dto;

import com.stretto.demo.features.internalUser.domain.enums.RolEnum;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

//DTO (Data Transfer Object) es un objeto que define qué datos entran y salen de tu API. No es la entidad, es lo que el usuario manda o recibe.
//Lo que el cliente manda para crear un usuario
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InternalUserDTORequest {
    @NotBlank(message = "Name is required")
    @Size(max = 50, message = "Name cannot exceed 100 characters")
    private String name;

    @NotBlank(message = "Email is required")
    @Email(message = "Email is not valid")
    private String email;

    @NotBlank(message = "Password is required")
    private String passwordHash;

    @NotBlank(message = "rol is required")
    private RolEnum rol;
}
