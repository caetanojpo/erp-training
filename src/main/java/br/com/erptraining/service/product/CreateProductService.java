package br.com.erptraining.service.product;

import br.com.erptraining.domain.Product;
import br.com.erptraining.dtos.product.CreateProductDTO;
import br.com.erptraining.enums.ProductSituation;
import br.com.erptraining.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CreateProductService {

    private final ProductRepository repository;

    @Transactional
    public Product save(CreateProductDTO productData) {

        Product product = Product.builder()
                .name(productData.name())
                .description(productData.description())
                .price(productData.price())
                .productType(productData.productType())
                .build();

        product.setProductSituation(ProductSituation.ACTIVATED);

        return repository.save(product);
    }
}
