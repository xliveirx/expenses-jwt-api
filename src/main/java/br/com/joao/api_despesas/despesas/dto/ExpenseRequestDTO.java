package br.com.joao.api_despesas.despesas.dto;

import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;

public record ExpenseRequestDTO(@NotNull String description, @NotNull LocalDate date, @NotNull BigDecimal amount) {
}
