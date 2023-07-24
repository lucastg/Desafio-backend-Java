package com.challenge.backend.model;

import com.challenge.backend.enums.OrderStatus;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Entity
@Data
public class OrderModel {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column
    private Long userId;

    @Column
    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    @Column
    private BigDecimal precoTotal;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "orderModel")
    private List<ItemModel> itens;
}
