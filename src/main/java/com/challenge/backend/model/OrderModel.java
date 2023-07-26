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

    public OrderModel() {

    }

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

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "order_id")
    private List<ItemModel> itens;


}
