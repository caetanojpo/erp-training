package br.com.erptraining.controller;

import br.com.erptraining.domain.Order;
import br.com.erptraining.domain.OrderItem;
import br.com.erptraining.dtos.order.DetailOrderDTO;
import br.com.erptraining.dtos.order.DiscountOrderDTO;
import br.com.erptraining.dtos.orderitem.CreateOrderItemDTO;
import br.com.erptraining.dtos.orderitem.UpdateOrderItemDTO;
import br.com.erptraining.mapper.OrderMapper;
import br.com.erptraining.service.order.FindOrderService;
import br.com.erptraining.service.order.OrderItemService;
import br.com.erptraining.service.order.UpdateOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderItemService orderItemService;
    private final FindOrderService find;
    private final UpdateOrderService update;

    private static final String ROUTE = "/api/orders/{id}";

    @PostMapping
    public ResponseEntity<DetailOrderDTO> create(@RequestBody CreateOrderItemDTO orderItemData, UriComponentsBuilder uriBuilder) {
        Order order = orderItemService.saveOnNewOrder(orderItemData);

        URI uri = uriBuilder.path(ROUTE).buildAndExpand(order.getId()).toUri();

        DetailOrderDTO detailedOrder = OrderMapper.INSTANCE.toDetailOrder(order);

        return ResponseEntity.created(uri).body(detailedOrder);
    }

    @PostMapping("/{id}")
    public ResponseEntity<DetailOrderDTO> addToExistingOrder(@PathVariable UUID id, @RequestBody CreateOrderItemDTO orderItemData, UriComponentsBuilder uriBuilder) {

        Order order = orderItemService.saveOnExistingOrder(orderItemData, id);

        URI uri = uriBuilder.path(ROUTE).buildAndExpand(order.getId()).toUri();

        DetailOrderDTO detailedOrder = OrderMapper.INSTANCE.toDetailOrder(order);

        return ResponseEntity.created(uri).body(detailedOrder);

    }

    @PutMapping("/discount/{id}")
    public ResponseEntity<DetailOrderDTO> applyDiscount(@PathVariable UUID id, @RequestBody DiscountOrderDTO discountData, UriComponentsBuilder uriBuilder) {
        Order order = update.applyDiscount(discountData, id);

        DetailOrderDTO detailedOrder = OrderMapper.INSTANCE.toDetailOrder(order);

        return ResponseEntity.ok(detailedOrder);
    }

    @PutMapping("/orderItem/{id}")
    public ResponseEntity<OrderItem> modifyQuantity(@PathVariable UUID id, @RequestBody UpdateOrderItemDTO orderItemData, UriComponentsBuilder uriBuilder) {
        OrderItem orderItem = orderItemService.modifyQuantity(id, orderItemData);

        return ResponseEntity.ok(orderItem);
    }

    @PutMapping("/orderItem/remove/{orderUUID}")
    public ResponseEntity<DetailOrderDTO> removeOrderItem(@PathVariable UUID orderUUID, @RequestBody UUID orderItemUUID) {
        Order order = update.removeOrderItem(orderUUID, orderItemUUID);

        DetailOrderDTO detailedOrder = OrderMapper.INSTANCE.toDetailOrder(order);

        return ResponseEntity.ok(detailedOrder);
    }

    @GetMapping()
    public ResponseEntity<Page<Order>> list(Pageable pagination) {
        Page<Order> ordersList = find.paginateList(pagination);
        return ResponseEntity.ok(ordersList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DetailOrderDTO> detail(@PathVariable UUID id) {
        Order order = find.byId(id);
        DetailOrderDTO detailedOrder = OrderMapper.INSTANCE.toDetailOrder(order);
        return ResponseEntity.ok(detailedOrder);
    }


}
