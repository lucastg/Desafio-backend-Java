package com.challenge.backend.repository;

import com.challenge.backend.dto.users.UserDto;
import reactor.core.publisher.Mono;

public interface UsersRepository {
    Mono<UserDto> getUserById(Long id);
}
