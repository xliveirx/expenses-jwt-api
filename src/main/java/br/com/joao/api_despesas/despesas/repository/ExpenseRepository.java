package br.com.joao.api_despesas.despesas.repository;

import br.com.joao.api_despesas.despesas.domain.expense.Expense;
import br.com.joao.api_despesas.despesas.domain.expense.Status;
import br.com.joao.api_despesas.despesas.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ExpenseRepository extends JpaRepository<Expense, Long> {
    List<Expense> findAllByUser(User logged);
    List<Expense> findAllByUserAndStatus(User logged, Status status);

    @Query("SELECT e from Expense e WHERE e.user = :logged AND FUNCTION('MONTH', e.date) = :month ")
    List<Expense> findAllByUserAndMonth(User logged, int month);
}
