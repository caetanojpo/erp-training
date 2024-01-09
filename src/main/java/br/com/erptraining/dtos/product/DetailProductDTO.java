package br.com.erptraining.dtos.product;

import br.com.erptraining.enums.ProductSituation;
import br.com.erptraining.enums.ProductType;

import java.math.BigDecimal;
import java.util.UUID;

public record DetailProductDTO(
        UUID id,
        String name,
        String description,
        BigDecimal price,
        ProductType productType,
        ProductSituation productSituation

        ) {
}
