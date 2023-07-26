package com.challenge.backend.repository.product;

import com.challenge.backend.dto.products.ProductsDto;
import com.challenge.backend.repository.BaseRepository;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

@Component
public class ProductsRepository extends BaseRepository {

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
