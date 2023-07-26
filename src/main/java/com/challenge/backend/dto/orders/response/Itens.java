package com.challenge.backend.dto.orders.response;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class Itens {
    public Long id;
    public BigDecimal price;
    public int amount;
    public BigDecimal partialAmount;

    public Itens(Long id, BigDecimal price, int amount, BigDecimal partialAmount) {
        this.id = id;
        this.price = price;
        this.amount = amount;
        this.partialAmount = partialAmount;
    }
}
