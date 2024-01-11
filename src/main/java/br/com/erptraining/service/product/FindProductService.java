package br.com.erptraining.service.product;

import br.com.erptraining.domain.Product;
import br.com.erptraining.exception.EntityNotFoundException;
import br.com.erptraining.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FindProductService {

    private final ProductRepository repository;

    public Product byId(UUID id) {
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(Product.class.getSimpleName(), id));
    }

    public List<Product> list() {
        return repository.findAll();
    }

    public Page<Product> paginateList(Pageable pagination) {
        return repository.findAll(pagination);
    }

}
