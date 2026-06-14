package com.stretto.demo.features.orderDetail;

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
    private OrderDetailMapper orderDetailMapper;

    //CREAR DETALLE DE ORDEN
    @Override
    @Transactional
    public OrderDetailDTOResponse create (Long orderId, OrderDetailDTORequest dto)
    {
        OrderEntity order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        ProductEntity product = productRepository.findById(dto.getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found"));

        List<FlavorsEntity> flavors = flavorsRepository.findAllById(dto.getFlavorsId());

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
        orderDetailRepository.deleteById(id);
    }
}
