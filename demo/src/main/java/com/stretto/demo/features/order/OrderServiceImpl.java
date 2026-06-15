package com.stretto.demo.features.order;

import com.stretto.demo.features.flavors.FlavorsRepository;
import com.stretto.demo.features.flavors.domain.FlavorsEntity;
import com.stretto.demo.features.internalUser.InternalUserRepository;
import com.stretto.demo.features.internalUser.domain.InternalUserEntity;
import com.stretto.demo.features.order.domain.OrderEntity;
import com.stretto.demo.features.order.domain.dto.DailyReportDTO;
import com.stretto.demo.features.order.domain.dto.MonthlyReportDTO;
import com.stretto.demo.features.order.domain.dto.OrderDTORequest;
import com.stretto.demo.features.order.domain.dto.OrderDTOResponse;
import com.stretto.demo.features.order.domain.enums.SaleChannelEnum;
import com.stretto.demo.features.order.domain.enums.StateOrderEnum;
import com.stretto.demo.features.order.domain.mapper.OrderMapper;
import com.stretto.demo.features.orderDetail.domain.OrderDetailEntity;
import com.stretto.demo.features.product.ProductRepository;
import com.stretto.demo.features.product.domain.ProductEntity;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final InternalUserRepository internalUserRepository;
    private final FlavorsRepository flavorsRepository;
    private final OrderMapper orderMapper;


    //CREAR PEDIDO
    @Override
    @Transactional
    public OrderDTOResponse create(OrderDTORequest request) {
        InternalUserEntity user = internalUserRepository.findById(request.getUserID())
                .orElseThrow(() -> new RuntimeException("User not found with id: " + request.getUserID()));

        OrderEntity order = orderMapper.toEntity(request, user);

        List<OrderDetailEntity> details = request.getDetails()
                .stream()
                .map(d -> {

                    ProductEntity product = productRepository.findById(d.getProductId())
                            .orElseThrow(() -> new RuntimeException("Product not found with id: " + d.getProductId()));

                    List<FlavorsEntity> flavors = flavorsRepository.findAllById(d.getFlavorsId());

                    if (product.getStock() < d.getQuantity())
                    {
                        throw new RuntimeException("Not enough stock for product id: " + d.getProductId());
                    }

                    if (flavors.size() != d.getFlavorsId().size()) {
                        throw new RuntimeException("One or more flavors not found with ids: " + d.getFlavorsId());
                    }

                    if (flavors.size() > product.getMaxFlavors()) {
                        throw new RuntimeException("The product" + product.getName() + "allows a maximum of" + product.getMaxFlavors() + "flavors");
                    }

                    product.setStock(product.getStock() - d.getQuantity());
                    productRepository.save(product);

                    return OrderDetailEntity.builder()
                            .quantity(d.getQuantity())
                            .unitPrice(product.getPrice())
                            .product(product)
                            .order(order)
                            .flavors(flavors)
                            .build();
                }).toList();

        order.setOrderDetails(details);

        BigDecimal total = details.stream()
                .map(detail -> detail.getUnitPrice()
                        .multiply(BigDecimal.valueOf(detail.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        order.setTotal(total);

        OrderEntity saved = orderRepository.save(order);
        return orderMapper.toResponse(saved);
    }

    //BUSCAR TODOS LOS PEDIDOS
    @Override
    public List<OrderDTOResponse> findAll() {
        return orderRepository.findAll()
                .stream()
                .map(orderMapper::toResponse)
                .toList();
    }

    //BUSCAR PEDIDO POR ID
    @Override
    public OrderDTOResponse findById(Long id) {
        OrderEntity entity = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + id));
        return orderMapper.toResponse(entity);
    }

    //ACTUALIZAR ESTADO DEL PEDIDO
    @Override
    @Transactional
    public OrderDTOResponse updateState(Long id, StateOrderEnum state) {
        OrderEntity entity = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + id));

        if (entity.getStateOrderEnum() == StateOrderEnum.CANCELLED) {
            throw new RuntimeException("Cannot update a cancelled order");
        }

        entity.setStateOrderEnum(state);

        return orderMapper.toResponse(entity);
    }

    //CANCELAR PEDIDO
    @Override
    @Transactional
    public void cancel(Long id) {
        OrderEntity entity = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + id));

        if (entity.getStateOrderEnum() == StateOrderEnum.CANCELLED) {
            throw new RuntimeException("Order is already cancelled");
        }

        for(OrderDetailEntity detail : entity.getOrderDetails())
        {
            ProductEntity product = detail.getProduct();

            product.setStock(product.getStock() + detail.getQuantity());
            productRepository.save(product);
        }

        entity.setStateOrderEnum(StateOrderEnum.CANCELLED);

        orderRepository.save(entity);
    }

    //BUSCAR PEDIDO POR USUARIO
    @Override
    public List<OrderDTOResponse> findByUser(Long userId) {
        return orderRepository.findByInternalUser_Id(userId)
                .stream()
                .map(orderMapper::toResponse)
                .toList();
    }

    //FILTRAR POR ESTADO
    @Override
    public List<OrderDTOResponse> findByState(StateOrderEnum state) {
        return orderRepository.findByStateOrderEnum(state)
                .stream()
                .map(orderMapper::toResponse)
                .toList();
    }

    //BUSCAR POR CANAL DE PEDIDO
    @Override
    public List<OrderDTOResponse> findBySaleChannel(SaleChannelEnum saleChannel) {
        return orderRepository.findBySaleChannel(saleChannel)
                .stream()
                .map(orderMapper::toResponse)
                .toList();
    }


    //BUSCAR ENTRE FECHAS
    @Override
    public List<OrderDTOResponse> findBetweenDates(LocalDate startDate,
                                                   LocalDate endDate) {

        LocalDateTime start = startDate.atStartOfDay();
        LocalDateTime end = endDate.atTime(LocalTime.MAX);

        return orderRepository.findByDateBetween(start, end)
                .stream()
                .map(orderMapper::toResponse)
                .toList();
    }


    //REPORTE DIARIO DE PEDIDOS PROGRAMADO
    @Scheduled(cron = "0 59 23 * * *")
    public void scheduledDailyReport()
    {
        generateDailyReport();
    }

    //REPORTE MENSUAL DE PEDIDOS PROGRAMADO
    @Scheduled (cron = "0 0 0 1 * *")
    public void scheduledMonthlyReport()
    {
        LocalDate previosMonth = LocalDate.now().minusMonths(1);

        int year = previosMonth.getYear();
        int month = previosMonth.getMonthValue();

        generateMonthlyReport(year,month);
    }

    //GENERAR REPORTE DIARIO
    @Override
    public DailyReportDTO generateDailyReport()
    {
        LocalDate today = LocalDate.now();

        LocalDateTime start = today.atStartOfDay();

        LocalDateTime end = today.atTime(LocalTime.MAX);

        List<OrderEntity> orders = orderRepository.findByDateBetween(start, end);

        int totalOrders = orders.size();

        int cancellOrders = (int) orders.stream()
                .filter(order -> order.getStateOrderEnum()
                        == StateOrderEnum.CANCELLED)
                .count();

        BigDecimal totalRevenue = orders.stream()
                .filter(order -> order.getStateOrderEnum() !=
                        StateOrderEnum.CANCELLED)
                .map(OrderEntity :: getTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);


        Map<SaleChannelEnum, BigDecimal> revenueByChannel = orders.stream()
                .filter(o -> o.getStateOrderEnum() != StateOrderEnum.CANCELLED)
                .collect(Collectors.groupingBy(
                        OrderEntity::getSaleChannelEnum,
                        Collectors.reducing(
                                BigDecimal.ZERO,
                                OrderEntity::getTotal,
                                BigDecimal::add
                        )
                ));

            return DailyReportDTO.builder()
                    .date(today)
                    .totalOrders(totalOrders)
                    .cancellOrders(cancellOrders)
                    .totalRevenue(totalRevenue)
                    .build();
    }

    //GENERAR REPORTE MENSUAL
    @Override
    public MonthlyReportDTO generateMonthlyReport(int year, int month) {
        LocalDate firsDay = LocalDate.of(year, month, 1);
        LocalDate lastDay = firsDay.withDayOfMonth(firsDay.lengthOfMonth());

        LocalDateTime start = firsDay.atStartOfDay();

        LocalDateTime end = lastDay.atTime(LocalTime.MAX);

        List<OrderEntity> orders = orderRepository.findByDateBetween(start, end);

        int totalOrders = orders.size();

        int cancellOrders = (int) orders.stream()
                .filter(order -> order.getStateOrderEnum()
                        == StateOrderEnum.CANCELLED)
                .count();

        BigDecimal totalRevenue = orders.stream()
                .filter(order -> order.getStateOrderEnum() !=
                        StateOrderEnum.CANCELLED)
                .map(OrderEntity :: getTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        Map<SaleChannelEnum, BigDecimal> revenueByChannel = orders.stream()
                .filter(o -> o.getStateOrderEnum() != StateOrderEnum.CANCELLED)
                .collect(Collectors.groupingBy(
                        OrderEntity::getSaleChannelEnum,
                        Collectors.reducing(
                                BigDecimal.ZERO,
                                OrderEntity::getTotal,
                                BigDecimal::add
                        )
                ));

        return MonthlyReportDTO.builder()
                .year(year)
                .month(month)
                .totalOrders(totalOrders)
                .cancelleOrders(cancellOrders)
                .totalRevenue(totalRevenue)
                .build();
    }
}
