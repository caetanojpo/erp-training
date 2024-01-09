package br.com.erptraining.service.order;

import br.com.erptraining.domain.OrderItem;
import br.com.erptraining.enums.OrderStatus;
import br.com.erptraining.enums.ProductType;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class UtilitiesOrderService {

    public OrderStatus getReadableStatus(String newStatus) {
        return Objects.equals(newStatus, "CANCELED") ?
                OrderStatus.CANCELED : Objects.equals(newStatus, "CLOSED") ?
                OrderStatus.CLOSED : OrderStatus.OPEN;
    }

    public boolean validateDiscountPermissionOnOrderItem(OrderItem orderItem){
        return orderItem.getProduct().getProductType().equals(ProductType.MATERIAL);
    }

    public boolean checkIfEligibleToReceiveDiscount(OrderItem orderItem, boolean actualDiscountPermission){
        boolean newOrderItemDiscountPermission = orderItem.getProduct().getProductType().equals(ProductType.MATERIAL);
        return newOrderItemDiscountPermission == actualDiscountPermission;
    }
}
