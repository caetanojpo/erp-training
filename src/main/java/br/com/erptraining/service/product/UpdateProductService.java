package br.com.erptraining.service.product;

import br.com.erptraining.domain.Product;
import br.com.erptraining.enums.ProductSituation;
import br.com.erptraining.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UpdateProductService {

    private final ProductRepository repository;
    private final FindProductService find;

    public Product changeProductSituation(UUID id, ProductSituation newSituation){
        Product product = find.byId(id);

        product.setProductSituation(newSituation);

        return repository.save(product);
    }
}
