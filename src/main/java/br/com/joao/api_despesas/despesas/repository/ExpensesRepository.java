package br.com.joao.api_despesas.despesas.repository;

import br.com.joao.api_despesas.despesas.domain.expenses.Expenses;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExpensesRepository extends JpaRepository<Expenses, Integer> {
}
