package br.com.joao.api_despesas.despesas.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.LocalDate;

public record ExpenseEditDTO(
    @Size(min = 1, max = 255, message = "Description must be between 1 and 255 characters")
    String description, 
    
    LocalDate date, 
    
    @DecimalMin(value = "0.01", message = "Amount must be greater than 0")
    BigDecimal amount
) {

}
