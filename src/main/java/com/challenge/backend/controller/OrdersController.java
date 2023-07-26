package com.challenge.backend.controller;

import com.challenge.backend.dto.orders.request.CreateOrdersRequest;
import com.challenge.backend.dto.orders.request.UpdateItensOrderRequest;
import com.challenge.backend.dto.orders.request.UpdateStatusOrderRequest;
import com.challenge.backend.dto.orders.response.OrdersResponse;
import com.challenge.backend.service.OrdersService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<OrdersResponse> updateStatusOrder(@RequestBody UpdateStatusOrderRequest updateStatusOrderRequest) {
        var updatedStatusOrder = ordersService.updateStatusOrder(updateStatusOrderRequest);

        return updatedStatusOrder.map(orderModel -> ResponseEntity.status(HttpStatus.OK)
                .body(new OrdersResponse(orderModel))).orElseGet(()
                -> ResponseEntity.notFound().build());
    }

    @PutMapping
    public ResponseEntity<OrdersResponse> updateItensOrder(@RequestBody UpdateItensOrderRequest updateItensOrderRequest) {
        var updatedItensOrder = ordersService.updateItensOrder(updateItensOrderRequest);

        return updatedItensOrder.map(orderModel -> ResponseEntity.status(HttpStatus.OK)
                .body(new OrdersResponse(orderModel))).orElseGet(()
                -> ResponseEntity.notFound().build());
    }

}
