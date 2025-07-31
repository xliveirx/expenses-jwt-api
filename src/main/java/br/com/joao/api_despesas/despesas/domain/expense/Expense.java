package br.com.joao.api_despesas.despesas.domain.expense;

import br.com.joao.api_despesas.despesas.domain.user.User;
import br.com.joao.api_despesas.despesas.dto.ExpenseRequestDTO;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "expenses")
@Getter
@Setter
public class Expense {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String description;
    private LocalDate date;
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    private Status status;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public Expense(ExpenseRequestDTO dto, User logged) {
        this.description = dto.description();
        this.date = dto.date();
        this.amount = dto.amount();
        this.status = Status.PENDING;
        this.user = logged;
    }

    public Expense() {}
}
