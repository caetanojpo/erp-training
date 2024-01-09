package br.com.erptraining.service.order;

import br.com.erptraining.domain.Order;
import br.com.erptraining.domain.OrderDiscount;
import br.com.erptraining.domain.OrderItem;
import br.com.erptraining.dtos.order.DiscountOrderDTO;
import br.com.erptraining.exception.DiscountOrderException;
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

    public Order update(OrderItem orderItem, UUID orderUUID) {
        Order order = find.byId(orderUUID);

        List<OrderItem> orderItems = order.getOrderItems();
        orderItems.add(orderItem);

        boolean discountViability = utilities.checkIfEligibleToReceiveDiscount(orderItem, order.getOrderDiscount().isDiscountPermission());

        if (!discountViability) {
            OrderDiscount discount = OrderDiscount.builder()
                    .discountPermission(false)
                    .discountValue(null)
                    .discountOrigin(null)
                    .discountPercent(null)
                    .discountDate(null)
                    .build();

            order.setOrderDiscount(discount);
        }

        BigDecimal currentOrderValue = order.getTotalOrder();
        order.setTotalOrder(currentOrderValue.add(orderItem.getTotalPrice()));

        repository.save(order);
        return order;
    }

    public void orderStatus(String newStatus, UUID orderUUID) {
        Order order = find.byId(orderUUID);
        order.setOrderStatus(utilities.getReadableStatus(newStatus));
    }

    public Order applyDiscount(DiscountOrderDTO discountData, UUID orderUUID) {
        boolean discountPermission = find.discountPermission(orderUUID);
        if (!discountPermission) {
            throw new DiscountOrderException("The present Order has a Service. Services are ineligible to receive a discount");
        }

        Order order = find.byId(orderUUID);

        if(order.getOrderDiscount().isDiscountApplied()) { throw new DiscountOrderException("Discount already applied to the present order!");}

        BigDecimal discountValue = discountData.discountValue() != null ? discountData.discountValue() : this.calculateDiscountValue(discountData.discountPercent(), order.getTotalOrder());

        OrderDiscount orderDiscount = OrderDiscount.builder()
                .discountPermission(true)
                .discountOrigin(discountData.discountOrigin())
                .discountPercent(discountData.discountPercent() != null ? discountData.discountPercent() : null)
                .discountValue(discountValue)
                .discountDate(LocalDateTime.now(ZoneId.systemDefault()))
                .discountApplied(true)
                .build();

        order.setOrderDiscount(orderDiscount);
        BigDecimal actualTotalValue = order.getTotalOrder();
        order.setTotalOrder(actualTotalValue.subtract(discountValue));

        repository.save(order);

        return order;

    }

    private BigDecimal calculateDiscountValue(BigDecimal percent, BigDecimal totalOrderValue) {
        return  totalOrderValue.multiply(percent.divide(new BigDecimal(100), 2, RoundingMode.HALF_UP))
                .setScale(2, RoundingMode.HALF_UP);

    }
}
