package br.com.joao.api_despesas.despesas.service;

import br.com.joao.api_despesas.despesas.domain.expense.Expense;
import br.com.joao.api_despesas.despesas.domain.user.User;
import br.com.joao.api_despesas.despesas.dto.ExpenseEditDTO;
import br.com.joao.api_despesas.despesas.dto.ExpenseRequestDTO;
import br.com.joao.api_despesas.despesas.dto.ExpenseResponseDTO;
import br.com.joao.api_despesas.despesas.exceptions.ApplicationException;
import br.com.joao.api_despesas.despesas.repository.ExpenseRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import br.com.joao.api_despesas.despesas.domain.expense.Status;

@Service
public class ExpenseService {

    private final ExpenseRepository expenseRepository;

    public ExpenseService(ExpenseRepository expenseRepository) {
        this.expenseRepository = expenseRepository;
    }

    @Transactional
    public Expense registerExpense(ExpenseRequestDTO dto, User logged) {
        if(dto.amount().compareTo(BigDecimal.ZERO) <= 0){
            throw new ApplicationException("Amount must be positive");
        }

        Expense expense = new Expense(dto, logged);
        return expenseRepository.save(expense);
    }

    public Page<ExpenseResponseDTO> getAllUserExpenses(User logged, Pageable pageable) {
        Page<Expense> expenses = expenseRepository.findAllByUser(logged, pageable);
        return expenses.map(ExpenseResponseDTO::new);
    }

    @Transactional
    public Expense editExpense(ExpenseEditDTO dto, Long id, User logged) {
        Expense expense = expenseRepository.findById(id)
                .orElseThrow(() -> new ApplicationException("Expense not found"));
        
        if (!expense.getUser().getId().equals(logged.getId())) {
            throw new ApplicationException("You can only edit your own expenses");
        }
        
        if (expense.getStatus() == Status.PAID) {
            throw new ApplicationException("Cannot edit paid expenses");
        }
        
        if (dto.date() != null) {
            if (dto.date().isBefore(LocalDate.now())) {
                throw new ApplicationException("Cannot set date in the past");
            }
            expense.setDate(dto.date());
        }
        
        if (dto.description() != null && !dto.description().trim().isEmpty()) {
            expense.setDescription(dto.description().trim());
        }
        
        if (dto.amount() != null) {
            if (dto.amount().compareTo(BigDecimal.ZERO) <= 0) {
                throw new ApplicationException("Amount must be positive");
            }
            expense.setAmount(dto.amount());
        }
        
        return expenseRepository.save(expense);
    }

    @Transactional
    public void alterStatus(User logged, Long id, Status status) {
        Expense expense = expenseRepository.findById(id)
                .orElseThrow(() -> new ApplicationException("Expense not found"));

        if(!expense.getUser().getId().equals(logged.getId())){
            throw new ApplicationException("You can only edit your own expenses");
        }
        expense.setStatus(status);
        expenseRepository.save(expense);
    }

    @Transactional
    public void deleteExpense(Long id, User logged) {
        Expense expense = expenseRepository.findById(id)
            .orElseThrow(() -> new ApplicationException("Expense not found"));

        if(!expense.getUser().getId().equals(logged.getId())){
            throw new ApplicationException("You can only edit your own expenses");
        }
        expenseRepository.delete(expense);
    }

    public Page<ExpenseResponseDTO> getPaidExpenses(User logged, Pageable pageable) {
        Page<Expense> expenses = expenseRepository.findAllByUserAndStatus(logged, Status.PAID, pageable);
        return expenses.map(ExpenseResponseDTO::new);
    }
    
    public Page<ExpenseResponseDTO> getUnpaidExpenses(User logged, Pageable pageable) {
        Page<Expense> expenses = expenseRepository.findAllByUserAndStatus(logged, Status.PENDING, pageable);
        return expenses.map(ExpenseResponseDTO::new);
    }

    public Page<ExpenseResponseDTO> getExpensesByMonth(User logged, int month, int year, Pageable pageable) {
        Page<Expense> expenses = expenseRepository.findAllByUserAndMonth(logged, month, year, pageable);
        return expenses.map(ExpenseResponseDTO::new);
    }
}
