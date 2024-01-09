package br.com.erptraining.dtos.order;

import br.com.erptraining.enums.DiscountOrigin;

import java.math.BigDecimal;

public record DiscountOrderDTO(
        BigDecimal discountPercent,
        BigDecimal discountValue,
        DiscountOrigin discountOrigin
) {
    public static DiscountOrderDTO withPercent(BigDecimal discountPercent, DiscountOrigin discountOrigin) {
        return new DiscountOrderDTO(discountPercent, null, discountOrigin);
    }

    public static DiscountOrderDTO withValue(BigDecimal discountValue, DiscountOrigin discountOrigin) {
        return new DiscountOrderDTO(null, discountValue, discountOrigin);
    }
}
