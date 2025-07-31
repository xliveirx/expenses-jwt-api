package br.com.joao.api_despesas.despesas.controller;

import br.com.joao.api_despesas.despesas.domain.expense.Expense;
import br.com.joao.api_despesas.despesas.domain.expense.Status;
import br.com.joao.api_despesas.despesas.domain.user.User;
import br.com.joao.api_despesas.despesas.dto.ExpenseEditDTO;
import br.com.joao.api_despesas.despesas.dto.ExpenseRequestDTO;
import br.com.joao.api_despesas.despesas.dto.ExpenseResponseDTO;
import br.com.joao.api_despesas.despesas.dto.StatusUpdateDTO;
import br.com.joao.api_despesas.despesas.service.ExpenseService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/expenses")
public class ExpenseController {

    private final ExpenseService expenseService;

    public ExpenseController(ExpenseService expenseService) {
        this.expenseService = expenseService;
    }

    @PostMapping
    public ResponseEntity<ExpenseResponseDTO> registerExpense(@Valid @RequestBody ExpenseRequestDTO dto,
                                                              @AuthenticationPrincipal User logged){

        Expense expense = expenseService.registerExpense(dto, logged);
        return ResponseEntity.created(URI.create("/expenses/" + expense.getId())).body(new ExpenseResponseDTO(expense));
    }

    @GetMapping
    public ResponseEntity<List<ExpenseResponseDTO>> getAllUserExpenses(@AuthenticationPrincipal User logged){
        return ResponseEntity.ok(expenseService.getAllUserExpenses(logged));
    }

    @PutMapping("/edit-expense/{id}")
    public ResponseEntity<ExpenseResponseDTO> editExpense(@AuthenticationPrincipal User logged,
                                                          @PathVariable Long id, 
                                                          @RequestBody ExpenseEditDTO dto){
        Expense expense = expenseService.editExpense(dto, id, logged);
        return ResponseEntity.ok(new ExpenseResponseDTO(expense));
    }

    @PatchMapping("/alter-status/{id}")
    public ResponseEntity<Void> alterStatus(@AuthenticationPrincipal User logged,
                                            @PathVariable Long id,
                                            @RequestBody StatusUpdateDTO dto){

        expenseService.alterStatus(logged, id, dto.status());
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteExpense(@PathVariable Long id, @AuthenticationPrincipal User logged){
        expenseService.deleteExpense(id, logged);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/paid")
    public ResponseEntity<List<ExpenseResponseDTO>> getPaidExpenses(@AuthenticationPrincipal User logged){
        return ResponseEntity.ok(expenseService.getPaidExpenses(logged));
    }

    @GetMapping("/pending")
    public ResponseEntity<List<ExpenseResponseDTO>> geteUnpaidExpenses(@AuthenticationPrincipal User logged){
        return ResponseEntity.ok(expenseService.getUnpaidExpenses(logged));
    }

    @GetMapping("/month/{month}")
    public ResponseEntity<List<ExpenseResponseDTO>> getMonthExpenses(@AuthenticationPrincipal User logged, @PathVariable int month){
        return ResponseEntity.ok(expenseService.getExpensesByMonth(logged, month));
    }
}
