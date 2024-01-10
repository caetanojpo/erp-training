package br.com.erptraining.service.order;

import br.com.erptraining.domain.Order;
import br.com.erptraining.exception.EntityNotFoundException;
import br.com.erptraining.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FindOrderService {

    private final OrderRepository repository;

    public Order byId(UUID id) {
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(Order.class.getSimpleName(), id));
    }

    public Integer lastOrderNumber() {
        List<Order> allOrder = repository.findAll();
        Collections.reverse(allOrder);
        if (allOrder.isEmpty()) return 0;
        return allOrder.get(0).getOrderNumber();
    }

    public boolean discountPermission(UUID id) {
        Order order = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(Order.class.getSimpleName(), id));

        return order.getOrderDiscount().isDiscountPermission();
    }
}
