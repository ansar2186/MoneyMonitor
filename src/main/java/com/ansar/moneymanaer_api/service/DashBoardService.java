package com.ansar.moneymanaer_api.service;

import com.ansar.moneymanaer_api.dto.IncomeDto;
import com.ansar.moneymanaer_api.dto.RecentTransactionDTO;
import com.ansar.moneymanaer_api.entity.ProfileEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.MailParseException;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.stream.Stream.concat;

@Service
@RequiredArgsConstructor
public class DashBoardService {

    private final ExpenseService expenseService;
    private final ProfileService profileService;
    private final IncomeService incomeService;
    private final CategoryService categoryService;

    public Map<String, Object> getDashBoardData() {

        ProfileEntity profile = profileService.getCurrentProfile();
        var returnValue = new LinkedHashMap<String, Object>();
        var latestIncome = incomeService.getLatestTopFiveIncomeCurrentUser();
        var latestExpense = expenseService.getLatestTopFiveExpensesCurrentUser();
        var latestTransactionDto = concat(latestIncome.stream().map(income -> RecentTransactionDTO.builder()
                        .id(income.getId())
                        .profileId(profile.getId())
                        .icon(income.getIcon())
                        .name(income.getName())
                        .amount(income.getAmount())
                        .date(income.getDate())
                        .createdAt(income.getCreated())
                        .updatedAt(income.getUpdated())
                        .type("Income")
                        .build()),
                latestExpense.stream().map(expense -> RecentTransactionDTO.builder()
                        .id(expense.getId())
                        .profileId(profile.getId())
                        .name(expense.getName())
                        .icon(expense.getIcon())
                        .amount(expense.getAmount())
                        .createdAt(expense.getCreated())
                        .updatedAt(expense.getUpdated())
                        .date(expense.getDate())
                        .type("Expense")
                        .build()))
                .sorted((a, b) -> {
                    int cmp = b.getDate().compareTo(a.getDate());
                    if (cmp == 0 && a.getCreatedAt() != null && b.getCreatedAt() != null) {
                        return b.getCreatedAt().compareTo(a.getCreatedAt());
                    }
                    return cmp;
                }).toList();

        returnValue.put("totalBalance", incomeService.getTotalIncomeCurrentUser().subtract(expenseService.getTotalExpensesCurrentUser()));
        returnValue.put("totalExpense", expenseService.getTotalExpensesCurrentUser());
        returnValue.put("totalIncome", incomeService.getTotalIncomeCurrentUser());
        returnValue.put("recent5Expense", latestExpense);
        returnValue.put("recent5Income", latestIncome);
        returnValue.put("recentTransaction", latestTransactionDto);
        return returnValue;
    }

}
