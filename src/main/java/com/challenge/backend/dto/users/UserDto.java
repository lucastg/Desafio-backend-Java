package com.challenge.backend.dto.users;

import lombok.Data;

@Data
public class UserDto {
    public Long id;
    public String email;
    public String username;
    public String password;
    public Name name;
    public String phone;

    @Data
    private static class Name {
        public String firstname;
        public String lastname;
    }
}
