package com.challenge.backend.dto.orders.request;

import lombok.Data;

import java.util.List;

@Data
public class CreateOrdersRequest {

    public Long userId;
    public List<ProductsRequest> products;

    @Data
    public static class ProductsRequest {
        public int id;
    }
}
