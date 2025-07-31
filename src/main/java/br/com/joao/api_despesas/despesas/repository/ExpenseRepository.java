package br.com.joao.api_despesas.despesas.repository;

import br.com.joao.api_despesas.despesas.domain.expense.Expense;
import br.com.joao.api_despesas.despesas.domain.expense.Status;
import br.com.joao.api_despesas.despesas.domain.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ExpenseRepository extends JpaRepository<Expense, Long> {
    Page<Expense> findAllByUser(User logged, Pageable pageable);
    Page<Expense> findAllByUserAndStatus(User logged, Status status, Pageable pageable);
    @Query("SELECT e from Expense e WHERE e.user = :logged AND EXTRACT(MONTH from e.date) = :month AND EXTRACT(YEAR FROM e.date) = :year")
    Page<Expense> findAllByUserAndMonth(User logged, int month, int year, Pageable pageable);
}
