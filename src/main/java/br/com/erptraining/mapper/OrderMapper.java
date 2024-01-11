package br.com.erptraining.mapper;

import br.com.erptraining.domain.Order;
import br.com.erptraining.dtos.order.DetailOrderDTO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface OrderMapper {

    OrderMapper INSTANCE = Mappers.getMapper(OrderMapper.class);

    DetailOrderDTO toDetailOrder(Order order);
}
