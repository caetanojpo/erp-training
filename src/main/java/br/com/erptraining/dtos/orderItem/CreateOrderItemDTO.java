package br.com.erptraining.dtos.orderItem;

import java.util.UUID;

public record CreateOrderItemDTO(
        UUID product_id,
        Integer quantity
) {
}
