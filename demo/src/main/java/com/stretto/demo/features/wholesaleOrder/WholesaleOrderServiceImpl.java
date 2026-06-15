package com.stretto.demo.features.wholesaleOrder;


import com.stretto.demo.common.exception.InvalidStateException;
import com.stretto.demo.common.exception.NotFoundException;
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

import java.math.BigDecimal;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class WholesaleOrderServiceImpl implements WholesaleOrderService {
    private final WholesaleOrderRepository wholesaleOrderRepository;
    private final RequestBudgetRepository requestBudgetRepository;
    private final WholesaleCustomerRepository wholesaleCustomerRepository;


    @Override
    public WholesaleOrderResponse  createFromRequestBudget(Long requestId){

        RequestBudgetEntity requestBudget= requestBudgetRepository.findById(requestId).orElseThrow(()->new RuntimeException("RequestBudget not found"));
        if(!requestBudget.getStateRequestEnum().equals(StateRequestEnum.CONFIRMED)){
            throw new InvalidStateException("An order can only be generated from a CONFIRMED request.");
        }

        wholesaleOrderRepository.findByRequestBudgetId(requestId).ifPresent(o->{
            throw new IllegalArgumentException("Only accepted request budgets can generate wholesale orders.");
        });

        WholesaleOrderEntity entity= WholesaleOrderMapper.toEntity(requestBudget,requestBudget.getCustomer());
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
        WholesaleOrderEntity entity= wholesaleOrderRepository.findById(id).orElseThrow(()->new NotFoundException("Wholesale Order not found"));
        return WholesaleOrderMapper.toResponse(entity);
    }

    @Override
    public void deleteWholesaleOrder(Long id){
        WholesaleOrderEntity entity= wholesaleOrderRepository.findById(id).orElseThrow(()->new NotFoundException("Wholesale Order not found"));
        entity.setActive(false);
    }

    @Override
    public WholesaleOrderResponse updateWholesaleOrder(Long id, WholesaleOrderRequest request){
        WholesaleOrderEntity entity=wholesaleOrderRepository.findById(id).orElseThrow(()->new NotFoundException("Wholesale Order not found"));
        if(!entity.isActive()){
            throw new InvalidStateException("Wholesale Order is inactive.");
        }
        if(request.getDeliveryDate() !=null){
            entity.setDeliveryDate(request.getDeliveryDate());
        }
        if(request.getDiscount() !=null){
            if(request.getDiscount().compareTo(BigDecimal.ZERO)<0){
                throw new InvalidStateException("Discount cannot be negative.");
            }
            entity.setDiscount(request.getDiscount());
        }
        return WholesaleOrderMapper.toResponse(entity);
    }

    @Override
    public WholesaleOrderResponse registerAdvancePayment(Long wholesaleOrderId, Long customerId, BigDecimal amount){
        WholesaleOrderEntity entity=wholesaleOrderRepository.findById(wholesaleOrderId).orElseThrow(()->new NotFoundException("Wholesale Order not found"));
        if(!entity.getWholesaleCustomer().getId().equals(customerId)){
            //bussines
            throw new InvalidStateException("The order does not belong to the specified customer.");
        }
        if(!entity.isAdvancePayment()){
            throw new InvalidStateException("This order does not require advance payment.");
        }
        if(entity.getAdvancePaymentAmount() != null){
            throw new InvalidStateException("Advance payment has already been registered.");
        }
        if(amount ==null || amount.compareTo(BigDecimal.ZERO)<0){
            throw new InvalidStateException("Advance payment amount must be greater than zero.");
        }
        entity.setAdvancePaymentAmount(amount);
        return WholesaleOrderMapper.toResponse(entity);
    }

    @Override
    @Transactional(readOnly = true)
    public List<WholesaleOrderResponse> getWholesaleOrdersByCustomerId(Long customerId){
        wholesaleCustomerRepository.findById(customerId).orElseThrow(()->new NotFoundException("Wholesale Customer not found with id: "+customerId));
        return wholesaleOrderRepository.findByWholesaleCustomerIdAndActiveTrue(customerId)
                .stream()
                .map(WholesaleOrderMapper::toResponse)
                .toList();
    }




}
