package br.com.erptraining.controller;

import br.com.erptraining.domain.Product;
import br.com.erptraining.dtos.product.CreateProductDTO;
import br.com.erptraining.dtos.product.DetailProductDTO;
import br.com.erptraining.mapper.ProductMapper;
import br.com.erptraining.service.product.CreateProductService;
import br.com.erptraining.service.product.FindProductService;
import jakarta.validation.Valid; //TODO import desnecessario
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
@RequestMapping("/api/product") //TODO seria interessante por o endpoint base no plural, pois indica que vc tem varios recursos para o mesmo (nao é obrigatorio)
@RequiredArgsConstructor
public class ProductController {

    private final CreateProductService create;
    private final FindProductService find;

    @PostMapping
    public ResponseEntity<DetailProductDTO> create(@RequestBody CreateProductDTO productData, UriComponentsBuilder uriBuilder) {
        Product product = create.save(productData);
        URI uri = uriBuilder.path("/api/product/{id}").buildAndExpand(product.getId()).toUri();

        DetailProductDTO deitaledProduct = ProductMapper.INSTANCE.toDetailProduct(product);

        return ResponseEntity.created(uri).body(deitaledProduct);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DetailProductDTO> detail(@PathVariable UUID id) {
        Product product = find.byId(id);

        //TODO ajustar a escrita em ingles do detailed
        DetailProductDTO deitaledProduct = ProductMapper.INSTANCE.toDetailProduct(product);

        return ResponseEntity.ok(deitaledProduct);
    }
}
