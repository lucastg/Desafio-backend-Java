package com.challenge.backend.model;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;

@Entity
@Data
public class ItemModel {
    public ItemModel(Long id, BigDecimal preco, int quantidade, BigDecimal precoParcial) {
        this.idProduto = id;
        this.preco = preco;
        this.quantidade = quantidade;
        this.precoParcial = precoParcial;
    }
    public ItemModel() {

    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column
    private Long idProduto;

    @Column
    private BigDecimal preco;

    @Column
    private int quantidade;

    @Column
    private BigDecimal precoParcial;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private OrderModel orderModel;
}
