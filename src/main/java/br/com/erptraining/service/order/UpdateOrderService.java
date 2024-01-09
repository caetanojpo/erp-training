package br.com.erptraining.service.order;

import br.com.erptraining.domain.Order;
import br.com.erptraining.domain.OrderDiscount;
import br.com.erptraining.domain.OrderItem;
import br.com.erptraining.dtos.order.DiscountOrderDTO;
import br.com.erptraining.exception.DiscountPermissionException;
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
    private final FindOrderService find;
    private final UtilitiesOrderService utilites;

    public Order update(OrderItem orderItem, UUID orderUUID) {
        Order order = find.byId(orderUUID);

        List<OrderItem> orderItems = order.getOrderItems();
        orderItems.add(orderItem);

        boolean discountViability = utilites.checkIfEligibleToReceiveDiscount(orderItem, order.getOrderDiscount().isDiscountPermission());

        if (!discountViability) {
            OrderDiscount discount = OrderDiscount.builder()
                    .discountPermission(false)
                    .build();

            order.setOrderDiscount(discount);
        }

        BigDecimal currentOrderValue = order.getTotalOrder();
        order.setTotalOrder(currentOrderValue.add(orderItem.getTotalPrice()));

        return order;
    }

    public void orderStatus(String newStatus, UUID orderUUID) {
        Order order = find.byId(orderUUID);
        order.setOrderStatus(utilites.getReadableStatus(newStatus));
    }

    public void applyDiscount(DiscountOrderDTO discountData, UUID orderUUID) {
        Order order = find.byId(orderUUID);
        if (!order.getOrderDiscount().isDiscountPermission()) {
            throw new DiscountPermissionException("The present Order has a Service. Services are ineligible to receive a discount");
        }
    }
}
