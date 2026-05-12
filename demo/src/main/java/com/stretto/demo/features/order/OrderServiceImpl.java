package com.stretto.demo.features.order;

import com.stretto.demo.features.order.domain.OrderEntity;
import com.stretto.demo.features.order.domain.dto.OrderDTORequest;
import com.stretto.demo.features.order.domain.dto.OrderDTOResponse;
import com.stretto.demo.features.order.domain.enums.StateOrderEnum;
import com.stretto.demo.features.order.domain.mapper.OrderMapper;
import com.stretto.demo.features.product.ProductRepository;
import com.stretto.demo.features.product.domain.ProductEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService{

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final InternalUserRepository internalUserRepository;

    @Override
    public OrderDTOResponse create(OrderDTORequest request) {
        InternalUser user = internalUserRepository.findById(request.getUserID())
                .orElseThrow(() -> new RuntimeException("User not found with id: " + request.getUserID()));

        List<ProductEntity> products = productRepository.findAllById(request.getProductIds());
        if (products.isEmpty()) {
            throw new RuntimeException("No valid products found");
        }

        BigDecimal total = products.stream()
                .map(ProductEntity::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        OrderEntity entity = OrderMapper.toEntity(request, user, products);
        entity.setStateOrderEnum(StateOrderEnum.PENDING);
        entity.setTotal(total);

        OrderEntity saved = orderRepository.save(entity);
        return OrderMapper.toResponse(saved);
    }

    @Override
    public List<OrderDTOResponse> findAll()
    {
        return orderRepository.findAll()
                .stream()
                .map(OrderMapper :: toResponse)
                .toList();
    }

    @Override
    public OrderDTOResponse findById (Long id)
    {
        OrderEntity entity = orderRepository.findById(id)
                .orElseThrow(()-> new RuntimeException("Order not found with id: " + id));
        return OrderMapper.toResponse(entity);
    }

    @Override
    public OrderDTOResponse updateState (Long id, StateOrderEnum state)
    {
        OrderEntity entity = orderRepository.findById(id)
                .orElseThrow(()-> new RuntimeException("Order not found with id: " + id));

        if(entity.getStateOrderEnum() == StateOrderEnum.CANCELLED)
        {
            throw  new RuntimeException("Cannot update a cancelled order");
        }

        entity.setStateOrderEnum(state);
        OrderEntity updated = orderRepository.save(entity);
        return OrderMapper.toResponse(updated);
    }

    @Override
    public void cancel (Long id)
    {
        OrderEntity entity = orderRepository.findById(id)
                .orElseThrow(()-> new RuntimeException("Order not found with id: " + id));
        if (entity.getStateOrderEnum() == StateOrderEnum.CANCELLED)
        {
            throw new RuntimeException()"Order is already cancelled";
        }

        entity.setStateOrderEnum(StateOrderEnum.CANCELLED);
        orderRepository.save(entity);
    }

    @Override
    public List<OrderDTOResponse> findByUser (Long userId)
    {
        return orderRepository.findByInternalUser_Id(userId)
                .stream()
                .map(OrderMapper :: toResponse)
                .toList();
    }
}
