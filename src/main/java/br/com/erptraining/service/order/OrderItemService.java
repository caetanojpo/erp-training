package br.com.erptraining.service.order;


import br.com.erptraining.domain.Order;
import br.com.erptraining.domain.OrderItem;
import br.com.erptraining.domain.Product;
import br.com.erptraining.dtos.orderitem.CreateOrderItemDTO;
import br.com.erptraining.repository.OrderItemRepository;
import br.com.erptraining.service.product.FindProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class OrderItemService {


    private final OrderItemRepository repository;
    private final FindProductService findProduct;
    private final CreateOrderService createOrder;
    private final UpdateOrderService updateOrder;


    @Transactional
    public Order saveOnNewOrder(CreateOrderItemDTO orderItemData) {

        OrderItem orderItem = formatOrderItem(orderItemData);

        Order order = createOrder.newOrder(orderItem);

        orderItem.setOrder(order);

        repository.save(orderItem);

        return order;
    }

    @Transactional
    public Order saveOnExistingOrder(CreateOrderItemDTO orderItemData, UUID orderUUID) {

        OrderItem orderItem = formatOrderItem(orderItemData);

        Order order = updateOrder.update(orderItem, orderUUID);

        orderItem.setOrder(order);

        repository.save(orderItem);

        return order;
    }


    private OrderItem formatOrderItem(CreateOrderItemDTO orderItemData) {

        Product product = findProduct.byId(orderItemData.product_id());

        OrderItem orderItem = OrderItem.builder()
                .product(product)
                .quantity(orderItemData.quantity()).build();

        BigDecimal totalValue = product.getPrice().multiply(BigDecimal.valueOf(orderItemData.quantity()));

        orderItem.setTotalPrice(totalValue);

        return orderItem;
    }
}