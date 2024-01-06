package br.com.erptraining.dtos.product;


import br.com.erptraining.enums.ProductType;

import java.math.BigDecimal;

public record CreateProductDTO(
        String name,
        String description,
        BigDecimal price,
        ProductType productType

) {
}
