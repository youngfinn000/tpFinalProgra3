package com.stretto.demo.features.wholesaleOrder;


import com.stretto.demo.features.order.OrderRepository;
import com.stretto.demo.features.order.domain.OrderEntity;
import com.stretto.demo.features.requestBudget.RequestBudgetRepository;
import com.stretto.demo.features.requestBudget.domain.RequestBudgetEntity;
import com.stretto.demo.features.requestBudget.domain.enums.StateRequestEnum;
import com.stretto.demo.features.wholesaleCustomer.WholesaleCustomerRepository;
import com.stretto.demo.features.wholesaleCustomer.domain.WholesaleCustomerEntity;
import com.stretto.demo.features.wholesaleOrder.domain.WholesaleOrderEntity;
import com.stretto.demo.features.wholesaleOrder.domain.dto.WholesaleOrderRequest;
import com.stretto.demo.features.wholesaleOrder.domain.dto.WholesaleOrderResponse;
import com.stretto.demo.features.wholesaleOrder.domain.mapper.WholesaleOrderMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class WholesaleOrderServiceImpl implements WholesaleOrderService {
    private final WholesaleOrderRepository wholesaleOrderRepository;
    private final RequestBudgetRepository requestBudgetRepository;
    private final WholesaleCustomerRepository wholesaleCustomerRepository;


    @Override
    public WholesaleOrderResponse  createFromRequestBudget(Long requestBudgetId){
        RequestBudgetEntity requestBudget= requestBudgetRepository.findById(requestBudgetId).orElseThrow(()->new RuntimeException("RequestBudget not found"));

        if(!requestBudget.getStateRequestEnum().equals(StateRequestEnum.CONFIRMED)){
            throw new IllegalStateException("An order can only be generated if it is confirmed. ");
        }
        wholesaleOrderRepository.findByRequestBudgetId(requestBudgetId).orElseThrow(()->new IllegalStateException("There is already an order with that id: "+ requestBudgetId));
        WholesaleCustomerEntity customer= requestBudget.getCustomer();

        WholesaleOrderEntity entity= WholesaleOrderEntity.builder()
                .advancePayment(requestBudget.isAdvancePayment())
                .deliveryDate(null)
                .discount(null)
                .active(true)
                .requestBudget(requestBudget)
                .wholesaleCustomer(customer)
                .build();

        return WholesaleOrderMapper.toResponse(wholesaleOrderRepository.save(entity));
    }

    @Override
    @Transactional(readOnly = true)
    public List<WholesaleOrderResponse> getallWholesaleOrders(){
        return wholesaleOrderRepository.findByActiveTrue()
                .stream()
                .map(WholesaleOrderMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public WholesaleOrderResponse getWholesaleOrderById(Long id){
        WholesaleOrderEntity entity= wholesaleOrderRepository.findById(id).orElseThrow(()->new RuntimeException("Wholesale Order not found whith id: "+id));
        return WholesaleOrderMapper.toResponse(entity);
    }

    @Override
    public void deleteWholesaleOrder(Long id){
        WholesaleOrderEntity entity= wholesaleOrderRepository.findById(id).orElseThrow(()->new RuntimeException("Wholesale Order not found with id: "+id));
        if(!entity.isActive()){
            throw new IllegalStateException("Wholesale Order is already inactive. ID: "+ id);
        }
        entity.setActive(false);
        wholesaleOrderRepository.save(entity);
    }

    @Override
    public WholesaleOrderResponse registerAdvancePayment(Long wholesaleOrderId, Long customerId){
        WholesaleOrderEntity entity= wholesaleOrderRepository.findById(wholesaleOrderId).orElseThrow(()->new RuntimeException("Wholesale order not found with id: "+wholesaleOrderId));
        if(!entity.getWholesaleCustomer().getId().equals(customerId)){
            throw new IllegalStateException("Wholesale Order does not belong to Wholesale Customer with id: "+customerId);
        }
        if(!entity.isActive()){
            throw new IllegalStateException("An inactive order cannot be modified. ");
        }
        if(entity.isAdvancePayment()){
            throw new IllegalStateException("The payment advance has already been registrated. ");
        }
        entity.setAdvancePayment(true);
        return WholesaleOrderMapper.toResponse(wholesaleOrderRepository.save(entity));

    }

    @Override
    @Transactional(readOnly = true)
    public List<WholesaleOrderResponse> getOrdersByCustomer (Long customerId){
        wholesaleOrderRepository.findById(customerId).orElseThrow(()->new RuntimeException("Wholesale Customer not found with id: "+customerId));
        return wholesaleOrderRepository.findByWholesaleCustomerIdAndActiveTrue(customerId)
                .stream()
                .map(WholesaleOrderMapper::toResponse)
                .toList();
    }


}
