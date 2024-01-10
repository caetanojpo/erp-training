package br.com.erptraining.mapper;

import br.com.erptraining.domain.Product;
import br.com.erptraining.dtos.product.DetailProductDTO;
import org.mapstruct.factory.Mappers;

public interface ProductMapper {

    ProductMapper INSTANCE = Mappers.getMapper(ProductMapper.class);

    DetailProductDTO toDetailProduct(Product product);
}
