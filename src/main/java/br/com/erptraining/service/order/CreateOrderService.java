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
        Integer lastOrderNumber = find.lastOrderNumber() + 1;

        List<OrderItem> orderItemList = List.of(orderItem);

        boolean discountPermission = utilities.validateDiscountPermissionOnOrderItem(orderItem);

        OrderDiscount discount = OrderDiscount.builder()
                .discountPermission(discountPermission)
                .build();

        //TODO crie a geração de objeto em outro método privado, para deixar esse método mais clean e com menos responsabilidade
        Order order = Order.builder()
                .orderNumber(lastOrderNumber)
                .orderItems(orderItemList)
                .orderDiscount(discount)
                .totalOrder(orderItem.getTotalPrice())
                .orderStatus(OrderStatus.OPEN)
                .build();

       return repository.save(order);

    }
}
