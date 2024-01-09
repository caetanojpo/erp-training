package br.com.erptraining.service.order;

import br.com.erptraining.domain.Order;
import br.com.erptraining.domain.OrderItem;
import br.com.erptraining.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UpdateOrderService {

    private final OrderRepository repository;
    private final UtilitiesOrderService utils;

    public Order update(OrderItem orderItem, UUID orderUUID) {
        Order order = repository.getReferenceById(orderUUID);

        List<OrderItem> orderItems = order.getOrderItems();
        orderItems.add(orderItem);

        BigDecimal currentOrderValue = order.getTotalOrder();
        order.setTotalOrder(currentOrderValue.add(orderItem.getTotalPrice()));

        return order;
    }

    public void orderStatus(String newStatus, UUID orderUUID) {
        Order order = repository.getReferenceById(orderUUID);
        order.setOrderStatus(utils.getReadableStatus(newStatus));
    }
}
