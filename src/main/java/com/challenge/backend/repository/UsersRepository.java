package com.challenge.backend.repository;

import com.challenge.backend.dto.users.UserDto;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

@Component
public class UsersRepository extends BaseRepository {
    public final String usersEndpoint = "/users/{id}";

    public UsersRepository(WebClient.Builder webClientBuilder) {
        super(webClientBuilder);
    }

    public Flux<UserDto> getUsers() {
        return webClient
                .get()
                .uri(usersEndpoint)
                .retrieve()
                .bodyToFlux(UserDto.class)
                .onErrorResume(error -> Flux.error(new Exception("Erro ao obter o usuário", error)));
    }

    public UserDto getUserById(Long id) {
        var user = new UserDto();
        user.setId(1L);
        return user;
//        return webClient
//                .get()
//                .uri(usersEndpoint, id)
//                .retrieve()
//                .bodyToMono(UserDto.class)
//                .onErrorResume(error -> Mono.error(new Exception("Erro ao obter o usuário", error)));
    }
}
