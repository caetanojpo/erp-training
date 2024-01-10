package br.com.erptraining.dtos.orderitem;

import br.com.erptraining.dtos.product.DetailProductDTO;

import java.math.BigDecimal;
import java.util.UUID;

public record DetailOrderItem(
        UUID id,
        DetailProductDTO product,
        Integer quantity,
        BigDecimal totalPrice
) {
}
