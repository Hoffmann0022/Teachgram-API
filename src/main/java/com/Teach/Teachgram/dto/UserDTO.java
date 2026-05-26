package com.Teach.Teachgram.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class UserDTO {

    public record SignupRequest(

        @NotBlank(message = "Nome é obrigatório")
        String name,

        @NotBlank(message = "Username é obrigatório")
        String userName,

        @NotBlank(message = "E-mail é obrigatório")
        String mail,

        String phone,

        @NotBlank(message = "Senha é obrigatória")
        @Size(min = 6, message = "Senha deve ter no mínimo 6 caracteres")
        String password,

        String profileLink,

        @Size(max = 200, message = "Bio pode ter no máximo 200 caracteres")
        String bio
    ) {}

    public record LoginRequest(
        @NotBlank(message = "Username é obrigatório")
        String userName,

        @NotBlank(message = "Senha é obrigatória")
        String password
    ) {}

    public record UpdateRequest(
        String name,
        String userName,
        String phone,
        String mail,
        String profileLink,

        @Size(max = 200, message = "Bio pode ter no máximo 200 caracteres")
        String bio
    ) {}

    public record UserResponse(
        Long id,
        String name,
        String userName,
        String phone,
        String mail,
        String profileLink,
        String bio,
        Boolean deleted,
        String createdAt,
        String updatedAt
    ) {}

    public record LoginResponse(
        String token,
        UserResponse user
    ) {}
}
