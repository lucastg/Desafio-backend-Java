package com.challenge.backend.repository;

import com.challenge.backend.dto.products.ProductsDto;

import reactor.core.publisher.Flux;

public interface ProductsRepository {

    Flux<ProductsDto> getProducts();
}
