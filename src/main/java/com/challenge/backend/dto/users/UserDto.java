package com.challenge.backend.dto.users;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserDto {

    public Long id;
    public String email;
    public String username;
    public String password;
    public Name name;
    public String phone;

    public UserDto(Long id) {
        this.id = id;
    }

    @Data
    private static class Name {
        public String firstname;
        public String lastname;
    }
}
