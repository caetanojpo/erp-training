package br.com.erptraining.domain;

import br.com.erptraining.enums.DiscountOrigin;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Embeddable
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDiscount {

    @Column(name = "discount_permission")
    private boolean discountPermission;

    @Column(name = "discount_percent")
    private BigDecimal discountPercent;

    @Column(name = "discount_value")
    private BigDecimal discountValue;

    @Column(name = "discount_origin")
    private DiscountOrigin discountOrigin;

    @Column(name = "discount_date")
    private LocalDateTime discountDate;

    @Column(name = "discount_applied")
    private boolean discountApplied;
}
