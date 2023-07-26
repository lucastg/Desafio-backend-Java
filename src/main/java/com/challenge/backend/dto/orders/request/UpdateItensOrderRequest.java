package com.challenge.backend.dto.orders.request;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
public class UpdateItensOrderRequest {
    public UUID id;
    public Long userId;
    public List<UpdateItem> items;


    @Data
    public static class UpdateItem {
        public int id;
        public BigDecimal price;

        public UpdateItem(int id, BigDecimal price) {
            this.id = id;
            this.price = price;
        }
    }

}