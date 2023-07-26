package com.challenge.backend.dto.orders.request;

import com.challenge.backend.enums.OrderStatus;
import lombok.Data;

import java.util.UUID;

@Data
public class UpdateStatusOrderRequest {

    private UUID id;
    private Long userId;
    private OrderStatus status;
}
