package br.com.joao.api_despesas.despesas.dto;

import jakarta.validation.constraints.NotNull;

public record UserRequestDTO(@NotNull String name, @NotNull String email,@NotNull String username, @NotNull String password, @NotNull String passwordConfirmation) {
}
