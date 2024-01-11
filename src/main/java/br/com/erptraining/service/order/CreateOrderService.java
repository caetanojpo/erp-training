package br.com.erptraining.service.order;

import br.com.erptraining.domain.Order;
import br.com.erptraining.domain.OrderDiscount;
import br.com.erptraining.domain.OrderItem;
import br.com.erptraining.enums.OrderStatus;
import br.com.erptraining.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList; //TODO import desnecessario
import java.util.List;

@Service
@RequiredArgsConstructor
public class CreateOrderService {

    private final OrderRepository repository;
    private final FindOrderService find;
    private final UtilitiesOrderService utilities;

    @Transactional
    public Order newOrder(OrderItem orderItem) {
        Integer newOrderNumber = find.lastOrderNumber() + 1;

        List<OrderItem> orderItemList = List.of(orderItem);

        boolean discountPermission = utilities.validateDiscountPermissionOnOrderItem(orderItem);

        OrderDiscount discount = OrderDiscount.builder()
                .discountPermission(discountPermission)
                .build();

        Order order = generateOrder(newOrderNumber, orderItemList, discount, orderItem);

       return repository.save(order);

    }

    private Order generateOrder(Integer orderNumber, List<OrderItem> orderItemList, OrderDiscount discount, OrderItem orderItem){
        return Order.builder()
                .orderNumber(orderNumber)
                .orderItems(orderItemList)
                .orderDiscount(discount)
                .totalOrder(orderItem.getTotalPrice())
                .orderStatus(OrderStatus.OPEN)
                .build();
    }
}
