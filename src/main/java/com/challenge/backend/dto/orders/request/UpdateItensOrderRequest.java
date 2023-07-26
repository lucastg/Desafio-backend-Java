package com.challenge.backend.dto.orders.request;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Data
public class UpdateItensOrderRequest {
    public UUID id;
    public Long userId;
    public List<UpdateItems> items;


    @Data
    public static class UpdateItems{
        public int id;
        public BigDecimal price;
    }

}