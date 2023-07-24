package com.challenge.backend.controller;

import com.challenge.backend.dto.orders.CreateOrdersRequest;
import com.challenge.backend.dto.orders.OrdersResponse;
import com.challenge.backend.dto.orders.UpdateStatusOrderRequest;
import com.challenge.backend.mapper.GenericMapper;
import com.challenge.backend.service.OrdersService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;

@RestController
@RequestMapping("/orders")
@AllArgsConstructor
public class OrdersController {

    public OrdersService ordersService;
    private final GenericMapper genericMapper;

    @PostMapping()
    public Optional<OrdersResponse> createOrder(@RequestBody CreateOrdersRequest createOrdersRequest) {
        var createdOrder = ordersService.createOrder(createOrdersRequest);
        return Optional.of(new OrdersResponse(createdOrder.get()));

    }

    @PatchMapping(value = "/{orderId}")
    public OrdersResponse updateStatusOrder(@PathVariable(value= "orderId") String orderId,
                                            @RequestBody @Valid UpdateStatusOrderRequest updateStatusOrderRequest) {
        return null;
    }
}
