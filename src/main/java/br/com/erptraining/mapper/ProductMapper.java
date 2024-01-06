package br.com.erptraining.mapper;

import br.com.erptraining.domain.Product;
import br.com.erptraining.dtos.product.CreateProductDTO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ProductMapper {

    ProductMapper INSTANCE = Mappers.getMapper(ProductMapper.class);

    Product toCreateProduct(CreateProductDTO productDTO);
}
