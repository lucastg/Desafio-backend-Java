package com.challenge.backend.dto.products;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
public class ProductsDto {

    public Long id;
    public String title;
    public BigDecimal price;
    public String description;
    public String category;

    public ProductsDto(Long id, BigDecimal price) {
        this.id = id;
        this.price = price;
    }
}
