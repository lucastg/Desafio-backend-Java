package com.challenge.backend.dto.orders;

import com.challenge.backend.enums.OrderStatus;
import com.challenge.backend.mapper.OrderMapper;
import com.challenge.backend.model.OrderModel;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Data
public class OrdersResponse {
    public UUID id;
    public Long userId;
    public OrderStatus status;
    public BigDecimal totalPrice;
    public List<Itens> items;

    public OrdersResponse(OrderModel orderModel) {
        this.id = orderModel.getId();
        this.userId = orderModel.getUserId();
        this.status = orderModel.getStatus();
        this.totalPrice = orderModel.getPrecoTotal();
        this.items = OrderMapper.toResponse(orderModel.getItens());
    }
}
