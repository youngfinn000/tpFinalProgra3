package com.stretto.demo.features.product.domain.dto;

import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductDTORequest {

    @NotBlank(message = "Product name is required")
    private String name;

    @NotNull(message = "Price is required")
    @DecimalMin(value = "0.0", message = "Price cannot be negative")
    private BigDecimal price;

    @NotNull(message = "Max flavors is required")
    @Min(1)
    @Max(4)
    private Integer maxFlavors;

    @Min(0)
    private Integer stock;
}
