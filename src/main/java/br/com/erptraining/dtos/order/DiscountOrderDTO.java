package br.com.erptraining.dtos.order;

import br.com.erptraining.enums.DiscountOrigin;

import java.math.BigDecimal;

public record DiscountOrderDTO(
        BigDecimal discountPercent,
        BigDecimal discountValue,
        DiscountOrigin discountOrigin
) {
}
