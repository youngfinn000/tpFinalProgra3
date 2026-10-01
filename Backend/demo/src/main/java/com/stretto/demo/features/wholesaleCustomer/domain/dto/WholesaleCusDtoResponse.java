package com.stretto.demo.features.wholesaleCustomer.domain.dto;


import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
    public class WholesaleCusDtoResponse {

        private Long id;
        private String companyName;
        private String email;
        private String contactName;
        private String cuit;
        private boolean active;

}
