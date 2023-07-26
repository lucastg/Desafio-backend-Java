package com.challenge.backend.repository.impl;

import com.challenge.backend.dto.products.ProductsDto;
import com.challenge.backend.repository.BaseRepository;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

@Component
public class ProductsRepository extends BaseRepository implements com.challenge.backend.repository.ProductsRepository {

    protected final String productEndpoint = "/products";

    public ProductsRepository(WebClient.Builder webClientBuilder) {
        super(webClientBuilder);
    }

    public Flux<ProductsDto> getProducts() {
        return webClient
                .get()
                .uri(productEndpoint)
                .retrieve()
                .bodyToFlux(ProductsDto.class)
                .onErrorResume(error -> Flux.error(new Exception("Erro ao obter o produto", error)));
    }
}
