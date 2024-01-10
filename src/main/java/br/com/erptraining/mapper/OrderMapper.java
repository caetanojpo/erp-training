package br.com.erptraining.mapper;

import br.com.erptraining.domain.Order;
import br.com.erptraining.dtos.order.DetailOrderDTO;
import org.mapstruct.factory.Mappers;

public interface OrderMapper {

    OrderMapper INSTANCE = Mappers.getMapper(OrderMapper.class);

    DetailOrderDTO toDetailOrder(Order order);
}
