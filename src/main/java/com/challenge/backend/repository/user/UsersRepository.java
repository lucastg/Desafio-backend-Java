package com.challenge.backend.repository.user;

import com.challenge.backend.dto.users.UserDto;
import com.challenge.backend.repository.BaseRepository;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
public class UsersRepository extends BaseRepository {
    public final String usersEndpoint = "/users/{id}";

    public UsersRepository(WebClient.Builder webClientBuilder) {
        super(webClientBuilder);
    }

    public Mono<UserDto> getUserById(Long id) {
        return webClient
                .get()
                .uri(usersEndpoint, id)
                .retrieve()
                .bodyToMono(UserDto.class)
                .onErrorResume(error -> Mono.error(new Exception("Erro ao obter o usuário", error)));
    }
}
