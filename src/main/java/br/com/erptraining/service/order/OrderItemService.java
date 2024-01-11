package br.com.erptraining.service.order;


import br.com.erptraining.domain.Order;
import br.com.erptraining.domain.OrderItem;
import br.com.erptraining.domain.Product;
import br.com.erptraining.dtos.orderitem.CreateOrderItemDTO;
import br.com.erptraining.dtos.orderitem.UpdateOrderItemDTO;
import br.com.erptraining.enums.ProductSituation;
import br.com.erptraining.exception.EntityNotFoundException;
import br.com.erptraining.exception.ProductException;
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
    private final FindOrderService findOrder;


    @Transactional
    public Order saveOnNewOrder(CreateOrderItemDTO orderItemData) {

        OrderItem orderItem = formatOrderItem(orderItemData);

        Order order = createOrder.newOrder(orderItem);

        orderItem.setOrder(order);

        return order;
    }

    @Transactional
    public Order saveOnExistingOrder(CreateOrderItemDTO orderItemData, UUID orderUUID) {

        OrderItem orderItem = formatOrderItem(orderItemData);

        Order order = updateOrder.addNewOrderItem(orderItem, orderUUID);

        orderItem.setOrder(order);

        return order;
    }

    public long isProductInOrder(UUID productId) {
        return findOrder.list().stream()
                .filter(order -> order.getOrderItems().stream()
                        .anyMatch(orderItem -> orderItem.getProduct().getId().equals(productId)))
                .count();
    }

    public OrderItem modifyQuantity(UUID id, UpdateOrderItemDTO orderItemData) {
        OrderItem orderItem = repository.findById(id).orElseThrow(() -> new EntityNotFoundException(OrderItem.class.getSimpleName(), id));

        orderItem.setQuantity(orderItemData.quantity());

        return repository.save((orderItem));
    }

    private OrderItem formatOrderItem(CreateOrderItemDTO orderItemData) {

        Product product = findProduct.byId(orderItemData.product_id());
        verifyProductStatus(product);
        BigDecimal totalValue = product.getPrice().multiply(BigDecimal.valueOf(orderItemData.quantity()));

        return OrderItem.builder()
                .product(product)
                .quantity(orderItemData.quantity())
                .totalPrice(totalValue)
                .build();
    }


    private void verifyProductStatus(Product product) {
        if (product.getProductSituation().equals(ProductSituation.DISABLED)) {
            throw new ProductException("The current product is DISABLED!");
        }
    }
}