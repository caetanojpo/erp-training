package br.com.erptraining.service.order;

import br.com.erptraining.domain.Order;
import br.com.erptraining.domain.OrderDiscount;
import br.com.erptraining.enums.OrderStatus;
import br.com.erptraining.exception.EntityNotFoundException;
import br.com.erptraining.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    public Page<Order> paginateList(Pageable pagination) {
        return repository.findAll(pagination);
    }

    public List<Order> list() {
        return repository.findAll();
    }


    public Integer lastOrderNumber() {
        List<Order> allOrder = repository.findAll();
        Collections.reverse(allOrder);
        if (allOrder.isEmpty()) return 0;
        return allOrder.get(0).getOrderNumber();
    }

    public boolean discountPermission(UUID id) {
        return repository.findById(id)
                .map(Order::getOrderDiscount)
                .map(OrderDiscount::isDiscountPermission)
                .orElseThrow(() -> new EntityNotFoundException(Order.class.getSimpleName(), id));
    }

    public boolean verifyOrderStatus(UUID id) {
        OrderStatus status = repository.findById(id)
                .map(Order::getOrderStatus)
                .orElseThrow(() -> new EntityNotFoundException(Order.class.getSimpleName(), id));

        return status.equals(OrderStatus.OPEN);
    }
}
