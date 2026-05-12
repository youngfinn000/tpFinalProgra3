package com.stretto.demo.features.product.domain.dto;

import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductDTOResponse {

    private Long id;
    private String name;
    private BigDecimal price;
    private boolean active;
    private Long stockId;
    private List<Long> flavorIds;
}
