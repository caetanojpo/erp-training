package br.com.erptraining.service.order;

import br.com.erptraining.enums.OrderStatus;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class UtilitiesOrderService {

    public OrderStatus getReadableStatus(String newStatus) {
        return Objects.equals(newStatus, "CANCELED") ?
                OrderStatus.CANCELED : Objects.equals(newStatus, "CLOSED") ?
                OrderStatus.CLOSED : OrderStatus.OPEN;
    }
}
