package br.com.erptraining.service.order;

import br.com.erptraining.domain.Order;
import br.com.erptraining.domain.OrderDiscount;
import br.com.erptraining.domain.OrderItem;
import br.com.erptraining.dtos.order.DiscountOrderDTO;
import br.com.erptraining.enums.OrderStatus;
import br.com.erptraining.exception.DiscountOrderException;
import br.com.erptraining.exception.OrderException;
import br.com.erptraining.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UpdateOrderService {

    private final OrderRepository repository;
    private final FindOrderService find;
    private final UtilitiesOrderService utilities;

    public Order addNewOrderItem(OrderItem orderItem, UUID orderUUID) {
        Order order = find.byId(orderUUID);

        verifyOrderStatus(order.getOrderStatus());

        List<OrderItem> orderItems = order.getOrderItems();
        orderItems.add(orderItem);

        boolean discountViability = utilities.checkIfEligibleToReceiveDiscount(orderItem, order.getOrderDiscount().isDiscountPermission());

        if (!discountViability) {
            OrderDiscount discount = OrderDiscount.builder()
                    .discountPermission(false)
                    .build();

            order.setOrderDiscount(discount);
        }

        BigDecimal currentOrderValue = order.getTotalOrder();
        order.setTotalOrder(currentOrderValue.add(orderItem.getTotalPrice()));

        return repository.save(order);
    }

    public Order removeOrderItem(UUID orderUUID, UUID orderItemUUID) {

        Order order = find.byId(orderUUID);

        order.getOrderItems().removeIf(orderItem -> orderItem.getId().equals(orderItemUUID));

        return repository.save(order);

    }

    public Order applyDiscount(DiscountOrderDTO discountData, UUID orderUUID) {

        Order order = find.byId(orderUUID);

        validateDiscountPossibility(order);

        BigDecimal discountValue = discountData.discountValue() != null ? discountData.discountValue() : this.calculateDiscountValue(discountData.discountPercent(), order.getTotalOrder());

        OrderDiscount orderDiscount = generateOrderDiscount(discountData, discountValue);

        order.setOrderDiscount(orderDiscount);
        BigDecimal actualTotalValue = order.getTotalOrder();
        order.setTotalOrder(actualTotalValue.subtract(discountValue));

        return repository.save(order);
    }


    private void verifyOrderStatus(OrderStatus status) {
        if (!status.equals(OrderStatus.OPEN)) {
            throw new OrderException("Order's status isn't OPEN. Can't make new changes");
        }
    }

    private void validateDiscountPossibility(Order order) {

        verifyOrderStatus(order.getOrderStatus());

        boolean discountPermission = find.discountPermission(order.getId());
        if (!discountPermission) {
            throw new DiscountOrderException("The present Order has a Service. Services are ineligible to receive a discount");
        }

        if (order.getOrderDiscount().isDiscountApplied()) {
            throw new DiscountOrderException("Discount already applied to the present order!");
        }

    }

    private BigDecimal calculateDiscountValue(BigDecimal percent, BigDecimal totalOrderValue) {
        return totalOrderValue.multiply(percent.divide(new BigDecimal(100), 2, RoundingMode.HALF_UP))
                .setScale(2, RoundingMode.HALF_UP);

    }

    private OrderDiscount generateOrderDiscount(DiscountOrderDTO discountData, BigDecimal discountValue) {
        return OrderDiscount.builder()
                .discountPermission(true)
                .discountOrigin(discountData.discountOrigin())
                .discountPercent(discountData.discountPercent() != null ? discountData.discountPercent() : null)
                .discountValue(discountValue)
                .discountDate(LocalDateTime.now(ZoneId.systemDefault()))
                .discountApplied(true)
                .build();
    }
}
