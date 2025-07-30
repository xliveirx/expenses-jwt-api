package br.com.joao.api_despesas.despesas.dto;

import jakarta.validation.constraints.NotNull;

public record LoginRequestDTO(@NotNull String email, @NotNull String password) {
}
