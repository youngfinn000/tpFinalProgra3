package com.stretto.demo.features.flavors.domain.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FlavorsDTORequest {

    @NotBlank(message = "Flavors name is required")
    @Size(max = 100, message = "Flavors name cannot exceed 100 characters")
    private String name;

}
