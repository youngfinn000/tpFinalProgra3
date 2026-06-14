package com.stretto.demo.features.product.domain.dto;

import com.stretto.demo.features.stock.domain.dto.StockDTOResponse;
import lombok.*;

import java.math.BigDecimal;


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
    private Integer maxFlavors;
    private Integer stock;

}
