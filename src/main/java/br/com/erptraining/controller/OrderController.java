package br.com.erptraining.controller;

import br.com.erptraining.domain.Order;
import br.com.erptraining.dtos.orderItem.CreateOrderItemDTO;
import br.com.erptraining.service.order.FindOrderService;
import br.com.erptraining.service.order.OrderItemService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("/api/order")
@RequiredArgsConstructor
public class OrderController {

    private final OrderItemService orderItemService;
    private final FindOrderService find;

    @PostMapping
    public ResponseEntity<Order> create(@RequestBody @Valid CreateOrderItemDTO orderItemData, UriComponentsBuilder uriBuilder) {
        Order order = orderItemService.saveOnNewOrder(orderItemData);

        URI uri = uriBuilder.path("/api/order/{id}").buildAndExpand(order.getId()).toUri();

        return ResponseEntity.created(uri).body(order);
    }

    @PostMapping("/{id}")
    public ResponseEntity<Order> addToExistingOrder(@PathVariable UUID id, @RequestBody @Valid CreateOrderItemDTO orderItemData, UriComponentsBuilder uriBuilder) {

        Order order = orderItemService.saveOnExistingOrder(orderItemData, id);

        URI uri = uriBuilder.path("/api/order/{id}").buildAndExpand(order.getId()).toUri();

        return ResponseEntity.created(uri).body(order);

    }

    @GetMapping("/{id}")
    public ResponseEntity<Order> detail(@PathVariable UUID id) {
        Order order = find.byId(id);
        return ResponseEntity.ok(order);
    }
}
