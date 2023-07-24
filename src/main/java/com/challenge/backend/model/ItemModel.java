package com.challenge.backend.model;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;

@Entity
@Data
public class ItemModel {
    public ItemModel(Long id, BigDecimal preco, int quantidade, BigDecimal precoParcial) {
        this.id = id;
        this.preco = preco;
        this.quantidade = quantidade;
        this.precoParcial = precoParcial;
    }

    @Id
    private Long id;

    @Column
    private BigDecimal preco;

    @Column
    private int quantidade;

    @Column
    private BigDecimal precoParcial;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private OrderModel orderModel;

    public ItemModel() {

    }
}
