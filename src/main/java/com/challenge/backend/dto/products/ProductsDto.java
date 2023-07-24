package com.challenge.backend.dto.products;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductsDto {
        public Long id;
        public String title;
        public BigDecimal price;
        public String description;
        public String category;
}
