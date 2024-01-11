package br.com.erptraining.controller;

import br.com.erptraining.domain.Product;
import br.com.erptraining.dtos.product.CreateProductDTO;
import br.com.erptraining.dtos.product.DetailProductDTO;
import br.com.erptraining.mapper.ProductMapper;
import br.com.erptraining.service.product.CreateProductService;
import br.com.erptraining.service.product.DeleteProductService;
import br.com.erptraining.service.product.FindProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final CreateProductService create;
    private final FindProductService find;
    private final DeleteProductService delete;

    private static final String ROUTES = "/api/products/{id}";

    @PostMapping
    public ResponseEntity<DetailProductDTO> create(@RequestBody CreateProductDTO productData, UriComponentsBuilder uriBuilder) {
        Product product = create.save(productData);
        URI uri = uriBuilder.path(ROUTES).buildAndExpand(product.getId()).toUri();

        DetailProductDTO detailedProduct = ProductMapper.INSTANCE.toDetailProduct(product);

        return ResponseEntity.created(uri).body(detailedProduct);
    }

    @GetMapping()
    public ResponseEntity<Page<Product>> list(Pageable pagination) {
        Page<Product> paginatedList = find.paginateList(pagination);

        return ResponseEntity.ok(paginatedList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DetailProductDTO> detail(@PathVariable UUID id) {
        Product product = find.byId(id);

        DetailProductDTO detailedProduct = ProductMapper.INSTANCE.toDetailProduct(product);

        return ResponseEntity.ok(detailedProduct);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteById(@PathVariable UUID id) {
        delete.excludeById(id);
        return ResponseEntity.ok("Product deleted!");
    }
}
