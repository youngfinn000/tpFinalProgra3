package com.stretto.demo.features.wholesaleCustomer.domain.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WholesaleCusDtoRequest {

    @Size(max =100,message = "Company name cannot exceed 100 characters")
    private String companyName;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

    @NotBlank(message = "Contact name is required")
    @Size (max = 100, message = "Contact name cannot exceed 100 charecters")
    private String contactName;

    @Size (max = 20, message = "CUIT cannot exceed 20 charecters")
    private String cuit;
}
