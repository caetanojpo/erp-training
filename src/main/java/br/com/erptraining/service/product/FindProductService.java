package br.com.erptraining.service.product;

import br.com.erptraining.domain.Product;
import br.com.erptraining.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FindProductService {

    private final ProductRepository repository;

    public Product byId(UUID id) {
        return repository.getReferenceById(id);
    }
}
