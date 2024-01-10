package br.com.erptraining.dtos.order;

import br.com.erptraining.domain.OrderDiscount;
import br.com.erptraining.dtos.orderitem.DetailOrderItem;
import br.com.erptraining.enums.OrderStatus;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public record DetailOrderDTO(
        UUID id,
        Integer orderNumber,
        List<DetailOrderItem> orderItems,
        OrderDiscount orderDiscount,
        BigDecimal totalOrder,
        OrderStatus orderStatus

) {
}
