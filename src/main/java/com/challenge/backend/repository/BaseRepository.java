package com.challenge.backend.repository;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class BaseRepository {

    public final WebClient webClient;

    public BaseRepository(WebClient.Builder webClientBuilder) {
        this.webClient =
                webClientBuilder
                .baseUrl("https://fakestoreapi.com")
                .build();
    }
}