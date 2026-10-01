package com.stretto.demo.features.wholesaleCustomer.domain.mapper;

import com.stretto.demo.features.wholesaleCustomer.domain.WholesaleCustomerEntity;
import com.stretto.demo.features.wholesaleCustomer.domain.dto.WholesaleCusDtoRequest;
import com.stretto.demo.features.wholesaleCustomer.domain.dto.WholesaleCusDtoResponse;
import org.springframework.stereotype.Component;

@Component
public class WholesaleCusMapper {

    public static WholesaleCustomerEntity toEntity (WholesaleCusDtoRequest request){

        return WholesaleCustomerEntity.builder()
                .companyName(request.getCompanyName())
                .email(request.getEmail())
                .contactName(request.getContactName())
                .cuit(request.getCuit())
                .active(true)
                .build();
    }

    public static WholesaleCusDtoResponse toResponse (WholesaleCustomerEntity entity){

        return WholesaleCusDtoResponse.builder()
                .id(entity.getId())
                .companyName(entity.getCompanyName())
                .email(entity.getEmail())
                .contactName(entity.getContactName())
                .cuit(entity.getCuit())
                .active(entity.isActive())
                .build();
    }
}
