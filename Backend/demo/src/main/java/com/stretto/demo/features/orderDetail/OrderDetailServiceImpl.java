package com.stretto.demo.features.orderDetail;

import com.stretto.demo.common.exception.FlavorLimitExceededException;
import com.stretto.demo.common.exception.NotFoundException;
import com.stretto.demo.features.flavors.FlavorsRepository;
import com.stretto.demo.features.flavors.domain.FlavorsEntity;
import com.stretto.demo.features.order.OrderRepository;
import com.stretto.demo.features.order.domain.OrderEntity;
import com.stretto.demo.features.orderDetail.domain.OrderDetailEntity;
import com.stretto.demo.features.orderDetail.domain.dto.OrderDetailDTORequest;
import com.stretto.demo.features.orderDetail.domain.dto.OrderDetailDTOResponse;
import com.stretto.demo.features.orderDetail.domain.mapper.OrderDetailMapper;
import com.stretto.demo.features.product.ProductRepository;
import com.stretto.demo.features.product.domain.ProductEntity;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderDetailServiceImpl implements OrderDetailService{

    private final OrderDetailRepository orderDetailRepository;
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final FlavorsRepository flavorsRepository;
    private final OrderDetailMapper orderDetailMapper;

    //CREAR DETALLE DE ORDEN
    @Override
    @Transactional
    public OrderDetailDTOResponse create (Long orderId, OrderDetailDTORequest dto)
    {
        OrderEntity order = orderRepository.findById(orderId)
                .orElseThrow(() -> new NotFoundException("Order not found"));

        ProductEntity product = productRepository.findById(dto.getProductId())
                .orElseThrow(() -> new NotFoundException("Product not found"));

        List<Long> requestedIds = dto.getFlavorsId();

        List<FlavorsEntity> flavors = flavorsRepository.findAllById(requestedIds);

        List<Long> foundIds = flavors.stream()
                .map(FlavorsEntity::getId)
                .toList();

        List<Long> missingIds = requestedIds.stream()
                .filter((id -> !foundIds.contains(id)))
                .toList();

        if(!missingIds.isEmpty()){
            throw new NotFoundException("Flavors not found with ids: " + missingIds);
        }

        if (flavors.size() > product.getMaxFlavors()) {
            throw new FlavorLimitExceededException(
                    "The product " + product.getName()
                            + " allows a maximum of "
                            + product.getMaxFlavors()
                            + " flavors"
            );
        }

        OrderDetailEntity entity = orderDetailMapper.toEntity(dto, product, flavors);

        entity.setOrder(order);

        OrderDetailEntity saved = orderDetailRepository.save(entity);

        return orderDetailMapper.toResponse(saved);
    }

    //BUSCA DETALLES POR EL ID DEL PEDIDO
    @Override
    public List<OrderDetailDTOResponse> getByOrder (Long orderId)
    {
        return orderDetailRepository.findByOrderId(orderId)
                .stream()
                .map(orderDetailMapper::toResponse)
                .toList();
    }

    //ELIMINA DETALLE DE ORDEN POR ID
    @Override
    @Transactional
    public void delete (Long id)
    {
        OrderDetailEntity entity = orderDetailRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Order detail not found with id: " + id));
        orderDetailRepository.delete(entity);
    }
}
