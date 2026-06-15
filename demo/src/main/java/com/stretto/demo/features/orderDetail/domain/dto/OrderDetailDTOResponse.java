package com.stretto.demo.features.orderDetail.domain.dto;

import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderDetailDTOResponse {

    private Long id;
    private Long productId;
    private String productName;
    private List<String> flavorNames;
    private Integer quantity;
    private BigDecimal unitPrice;

}
