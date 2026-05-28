package com.Teach.Teachgram.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class PostDTO {

    public record CreateRequest(
            @NotBlank(message = "Título é obrigatório")
            @Size(max = 50, message = "Título pode ter no máximo 50 caracteres")
            String title,

            @Size(max = 200, message = "Descrição pode ter no máximo 200 caracteres")
            String description,

            String photoLink,
            String videoLink,

            @NotNull(message = "Privacidade é obrigatória")
            Boolean isPrivate,

            @NotNull(message = "Id do usuário é obrigatório")
            Long userId
    ) {}

    public record PostResponse(
            Long id,
            String title,
            String description,
            String photoLink,
            String videoLink,
            Boolean isPrivate,
            Integer likes,
            Boolean deleted,
            String createdAt,
            String updatedAt,
            Long userId,
            String userName
    ) {}
}