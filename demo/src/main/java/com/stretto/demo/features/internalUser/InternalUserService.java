package com.stretto.demo.features.internalUser;

import com.stretto.demo.features.internalUser.domain.dto.InternalUserDTORequest;
import com.stretto.demo.features.internalUser.domain.dto.InternalUserDTOResponse;
import com.stretto.demo.features.order.domain.dto.OrderDTORequest;
import com.stretto.demo.features.order.domain.dto.OrderDTOResponse;
import com.stretto.demo.features.productionLot.domain.dto.ProductionLotDTORequest;
import com.stretto.demo.features.productionLot.domain.dto.ProductionLotDTOResponse;


import java.util.List;

public interface InternalUserService {

    InternalUserDTOResponse create(InternalUserDTORequest request);

    List<InternalUserDTOResponse> findAll();

    InternalUserDTOResponse findById(Long id);

    InternalUserDTOResponse update(Long id, InternalUserDTORequest request);

    void delete(Long id);

    InternalUserDTOResponse activate(Long id);

    OrderDTOResponse createOrder(Long userId, OrderDTORequest request);

    ProductionLotDTOResponse registerLot(Long userId, ProductionLotDTORequest request);
}
