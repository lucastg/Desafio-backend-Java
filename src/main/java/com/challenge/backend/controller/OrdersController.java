package com.challenge.backend.controller;

import com.challenge.backend.dto.orders.CreateOrdersRequest;
import com.challenge.backend.dto.orders.OrdersResponse;
import com.challenge.backend.dto.orders.UpdateItensOrderRequest;
import com.challenge.backend.dto.orders.UpdateStatusOrderRequest;
import com.challenge.backend.service.OrdersService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/orders")
@AllArgsConstructor
public class OrdersController {

    private final OrdersService ordersService;


    @PostMapping()
    public ResponseEntity<OrdersResponse> createOrder(@RequestBody CreateOrdersRequest createOrdersRequest) {
        var createdOrder = ordersService.createOrder(createOrdersRequest);

        return createdOrder.map(orderModel -> ResponseEntity.status(HttpStatus.OK)
                .body(new OrdersResponse(orderModel))).orElseGet(()
                -> ResponseEntity.notFound().build());

    }

    @PatchMapping()
    public Optional<OrdersResponse> updateStatusOrder(@RequestBody UpdateStatusOrderRequest updateStatusOrderRequest) {
        var updatedOrder = ordersService.updateStatusOrder(updateStatusOrderRequest);
        System.out.println(updatedOrder);
        return Optional.of(new OrdersResponse(updatedOrder.get()));
    }

    @PutMapping
    public Optional<OrdersResponse> updateItensOrder(UpdateItensOrderRequest updateItensOrderRequest) {
        var updatedOrder = ordersService.updateItensOrder(updateItensOrderRequest);
        return Optional.of(new OrdersResponse(updatedOrder.get()));
    }

}
