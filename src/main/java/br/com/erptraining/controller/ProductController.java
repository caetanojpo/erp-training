package br.com.erptraining.controller;

import br.com.erptraining.domain.Product;
import br.com.erptraining.dtos.product.CreateProductDTO;
import br.com.erptraining.service.product.CreateProductService;
import br.com.erptraining.service.product.FindProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("/api/product")
@RequiredArgsConstructor
public class ProductController {

    private final CreateProductService create;
    private final FindProductService find;

    @PostMapping
    public ResponseEntity<Product> create(@RequestBody CreateProductDTO productData, UriComponentsBuilder uriBuilder) {
        Product product = create.save(productData);
        URI uri = uriBuilder.path("/api/product/{id}").buildAndExpand(product.getId()).toUri();
        return ResponseEntity.created(uri).body(product);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> detail(@PathVariable UUID id) {
        Product product = find.byId(id);
        return ResponseEntity.ok(product);
    }
}
