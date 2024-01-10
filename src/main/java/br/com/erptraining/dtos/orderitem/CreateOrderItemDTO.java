package br.com.erptraining.dtos.orderitem;

import java.util.UUID;

public record CreateOrderItemDTO(
        UUID product_id,
        Integer quantity
) {
}
