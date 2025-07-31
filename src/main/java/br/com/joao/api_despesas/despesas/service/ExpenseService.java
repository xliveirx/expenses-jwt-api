package br.com.joao.api_despesas.despesas.service;

import br.com.joao.api_despesas.despesas.domain.expense.Expense;
import br.com.joao.api_despesas.despesas.domain.user.User;
import br.com.joao.api_despesas.despesas.dto.ExpenseEditDTO;
import br.com.joao.api_despesas.despesas.dto.ExpenseRequestDTO;
import br.com.joao.api_despesas.despesas.dto.ExpenseResponseDTO;
import br.com.joao.api_despesas.despesas.exceptions.ApplicationException;
import br.com.joao.api_despesas.despesas.repository.ExpenseRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
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

    public List<ExpenseResponseDTO> getAllUserExpenses(User logged) {
        var expenses = expenseRepository.findAllByUser(logged);

        if(expenses.isEmpty()){
            throw new ApplicationException("There is no expenses associated with the user");
        }
        return expenses.stream()
                .map(ExpenseResponseDTO::new)
                .toList();
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

    public List<ExpenseResponseDTO> getPaidExpenses(User logged) {
        var expenses = expenseRepository.findAllByUserAndStatus(logged, Status.PAID);
        return expenses.stream()
                .map(ExpenseResponseDTO::new)
                .toList();
    }
    public List<ExpenseResponseDTO> getUnpaidExpenses(User logged) {
        var expenses = expenseRepository.findAllByUserAndStatus(logged, Status.PENDING);
        return expenses.stream()
                .map(ExpenseResponseDTO::new)
                .toList();
    }

    public List<ExpenseResponseDTO> getExpensesByMonth(User logged, int month) {
        var expenses = expenseRepository.findAllByUserAndMonth(logged, month);

        if(expenses.isEmpty()){
            throw new ApplicationException("There is no expenses associated with the user on month " + month);
        }

        return expenses.stream()
                .map(ExpenseResponseDTO::new)
                .toList();
    }
}
