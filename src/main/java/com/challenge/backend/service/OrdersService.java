package com.challenge.backend.service;

import com.challenge.backend.dto.orders.request.CreateOrdersRequest;
import com.challenge.backend.dto.orders.request.UpdateItensOrderRequest;
import com.challenge.backend.dto.orders.request.UpdateStatusOrderRequest;
import com.challenge.backend.model.OrderModel;

import java.util.Optional;

public interface OrdersService {

    Optional<OrderModel> createOrder(CreateOrdersRequest createOrdersRequest);

    Optional<OrderModel> updateStatusOrder(UpdateStatusOrderRequest updateStatusOrderRequest);

    Optional<OrderModel> updateItensOrder(UpdateItensOrderRequest updateItensOrderRequest);

}
