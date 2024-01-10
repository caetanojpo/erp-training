package br.com.erptraining.service.order;

import br.com.erptraining.domain.OrderItem;
import br.com.erptraining.enums.ProductType;
import org.springframework.stereotype.Service;

@Service
public class UtilitiesOrderService {

    public boolean validateDiscountPermissionOnOrderItem(OrderItem orderItem) {
        return orderItem.getProduct().getProductType().equals(ProductType.MATERIAL);
    }

    public boolean checkIfEligibleToReceiveDiscount(OrderItem orderItem, boolean actualDiscountPermission) {
        boolean newOrderItemDiscountPermission = orderItem.getProduct().getProductType().equals(ProductType.MATERIAL);
        return newOrderItemDiscountPermission == actualDiscountPermission;
    }
}
