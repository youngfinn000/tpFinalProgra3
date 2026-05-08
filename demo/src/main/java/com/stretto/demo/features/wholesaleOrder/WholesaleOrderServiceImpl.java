package com.stretto.demo.features.wholesaleOrder;


import com.stretto.demo.features.order.OrderRepository;
import com.stretto.demo.features.order.domain.OrderEntity;
import com.stretto.demo.features.requestBudget.RequestBudgetRepository;
import com.stretto.demo.features.requestBudget.domain.RequestBudgetEntity;
import com.stretto.demo.features.wholesaleCustomer.WholesaleCustomerRepository;
import com.stretto.demo.features.wholesaleCustomer.domain.WholesaleCustomerEntity;
import com.stretto.demo.features.wholesaleOrder.domain.WholesaleOrderEntity;
import com.stretto.demo.features.wholesaleOrder.domain.dto.WholesaleOrderRequest;
import com.stretto.demo.features.wholesaleOrder.domain.dto.WholesaleOrderResponse;
import com.stretto.demo.features.wholesaleOrder.domain.mapper.WholesaleOrderMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WholesaleOrderServiceImpl implements WholesaleOrderService {
    private final WholesaleOrderRepository wholesaleOrderRepository;
    private final OrderRepository orderRepository;
    private final RequestBudgetRepository requestBudgetRepository;
    private final WholesaleCustomerRepository wholesaleCustomerRepository;

    //Create wsorder
    @Override
    public WholesaleOrderResponse  createWholesaleOrder(WholesaleOrderRequest request){
        OrderEntity order= orderRepository.findById(request.getOrderId()).orElseThrow(()->new RuntimeException("Order not found"));
        RequestBudgetEntity requestBudget= requestBudgetRepository.findById(request.getRequestBudgetId()).orElseThrow(()->new RuntimeException("RequestBudget not found"));
        WholesaleCustomerEntity wholesaleCustomer= wholesaleCustomerRepository.findById(request.getCustomerId()).orElseThrow(()->new RuntimeException("Customer not found"));
        WholesaleOrderEntity entity = WholesaleOrderMapper.toEntity(request,order,requestBudget,wholesaleCustomer);
        return WholesaleOrderMapper.toResponse(wholesaleOrderRepository.save(entity));
    }

    @Override
    public List<WholesaleOrderResponse> getallWholesaleOrders(){
        return wholesaleOrderRepository.findAll()
                .stream()
                .map(WholesaleOrderMapper::toResponse)
                .toList();
    }

    @Override
    public WholesaleOrderResponse getWholesaleOrderById(Long id){
        WholesaleOrderEntity entity= wholesaleOrderRepository.findById(id).orElseThrow(()->new RuntimeException("Wholesale Order not found"));
        return WholesaleOrderMapper.toResponse(entity);
    }

    @Override
    public void deleteWholesaleOrder(Long id){
        WholesaleOrderEntity entity=
                wholesaleOrderRepository.findById(id).orElseThrow(()->new RuntimeException("Wholesale Order not found"));
        wholesaleOrderRepository.delete(entity);
    }



}
