package br.com.joao.api_despesas.despesas.dto;

import br.com.joao.api_despesas.despesas.domain.expense.Expense;
import br.com.joao.api_despesas.despesas.domain.expense.Status;
import br.com.joao.api_despesas.despesas.domain.user.User;

import java.math.BigDecimal;
import java.time.LocalDate;

public record ExpenseResponseDTO(Long id, String description, LocalDate date, BigDecimal amount, Status status, User user) {
    public ExpenseResponseDTO(Expense expense) {
        this(expense.getId(), expense.getDescription(), expense.getDate(), expense.getAmount(), expense.getStatus(), expense.getUser());
    }
}
