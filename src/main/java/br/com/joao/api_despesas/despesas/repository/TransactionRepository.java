package br.com.joao.api_despesas.despesas.repository;

import br.com.joao.api_despesas.despesas.domain.transaction.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transaction, Integer> {
}
