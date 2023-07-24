package com.challenge.backend.repository;

import com.challenge.backend.dto.products.ProductsDto;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Component
public class ProductsRepository extends BaseRepository {

    protected final String productEndpoint = "/products/{id}";

    public ProductsRepository(WebClient.Builder webClientBuilder) {
        super(webClientBuilder);
    }

    public List<ProductsDto> getProducts() {
        var list = new ArrayList<ProductsDto>();

        var produto = new ProductsDto();
        produto.setId(1L);
        produto.setPrice(BigDecimal.valueOf(10.00));
        list.add(produto);

        var produtoDois = new ProductsDto();
        produtoDois.setId(2L);
        produtoDois.setPrice(BigDecimal.valueOf(100.00));
        list.add(produtoDois);

        return list;


//        return webClient
//                .get()
//                .uri(productEndpoint)
//                .retrieve()
//                .bodyToFlux(ProductsDto.class)
//                .onErrorResume(error -> Flux.error(new Exception("Erro ao obter o usuário", error)));
    }

    public ProductsDto getPoduct(Long id) {
        return new ProductsDto();
//        return webClient
//                .get()
//                .uri(productEndpoint, id)
//                .retrieve()
//                .bodyToMono(ProductsDto.class)
//                .onErrorResume(error -> Mono.error(new Exception("Erro ao obter o produto", error)));
    }
}
