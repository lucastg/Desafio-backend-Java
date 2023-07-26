package com.challenge.backend.dto.orders.request;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class CreateOrdersRequest {

    public Long userId;
    public List<ProductsRequest> products;

    @Data
    @NoArgsConstructor
    public static class ProductsRequest {
        public int id;

        public ProductsRequest(int id) {
            this.id = id;
        }
    }

}
