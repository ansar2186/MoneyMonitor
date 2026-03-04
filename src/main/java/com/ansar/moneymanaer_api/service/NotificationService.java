package com.ansar.moneymanaer_api.service;

import com.ansar.moneymanaer_api.dto.ExpenseDTO;
import com.ansar.moneymanaer_api.repository.ProfileRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.annotation.Schedules;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationService {

    private final ProfileRepository profileRepository;
    private final EmailService emailService;
    private final ExpenseService expenseService;
    @Value("${money.manager.frontend.url}")
    private String frontendUrl;

   @Scheduled(cron = "0 0 14 * * *", zone = "IST")
    //@Scheduled(cron = "0 * * * * *", zone = "IST")
    public void sendDailyIncomeExpenseReminder() {
        log.info("Job started : sendDailyIncomeExpenseReminder");

        profileRepository.findAll().forEach(profile -> {
            String body = "Hi " + profile.getFullName() + ",<br><br>"
                    + "This is a friendly reminder to add your income and expense for today in money manager. <br><br>"
                    + "<a href =" + frontendUrl + " style ='display:inline-block;padding:10px 20px;background-color:#4CAF50;color:#fff;text-decoration:none;border-radius:5px;font-weight:bold;'> Go to Money Manager </a>"
                    + "<br><br>Best regards,<br>Money Manager Team";
            emailService.sendEmail(profile.getEmail(),"Daily reminder:Add your income and expense for today in money manager",body);
         log.info("Job ended : sendDailyIncomeExpenseReminder");
        });

    }
   @Scheduled(cron = "0 0 23 * * *", zone = "IST")
   // @Scheduled(cron = "0 * * * * *", zone = "IST")
    public void sendDailyExpenseSummary(){
        log.info("Job started : sendDailyExpenseSummary");
        profileRepository.findAll().forEach(profile -> {
            List<ExpenseDTO> expenseDto = expenseService.getExpenseForUserOnDate(profile.getId(), LocalDate.now());
            log.info("Expense for user on date : " + expenseDto.toString());
            if(!expenseDto.isEmpty()){
                StringBuilder table= new StringBuilder();
                table.append("<table style='border-collapse:collapse;width:100%;'>");
                table.append("<tr style='background-color:#f2f2f2;'><th style='border:1px solid #ddd;padding:8px;'>SR.No</th><th style='border:1px solid #ddd;padding:8px;'>Name</th><th style='border:1px solid #ddd;padding:8px;'>Amount</th><th style='border:1px solid #ddd;padding:8px;'>Category</th></tr>");
                int i =1;
                for (ExpenseDTO expense : expenseDto ){
                    table.append("<tr>");
                    table.append("<td style='border:1px solid #add;padding:8px;'>").append(i++).append("</td>");
                    table.append("<td style='border:1px solid #add;padding:8px;'>").append(expense.getName()).append("</td>");
                    table.append("<td style='border:1px solid #add;padding:8px;'>").append(expense.getAmount()).append("</td>");
                    table.append("<td style='border:1px solid #add;padding:8px;'>").append(expense.getCategoryId()!=null?expense.getCategoryName():"N/A").append("</td>");
                   // table.append("<td style='border:1px solid #add;padding:8px;'>").append(expense.getCreated().append("</td>");
                    table.append("</tr>");
                }
                table.append("</table>");
                String body="Hi " +profile.getFullName()+", <br/><br/> Here is Summary of yours expenses for  today: <br/><br/>"+table+"<br/><br/>Best regards,<br/><br/>Money Manager Team";
                emailService.sendEmail(profile.getEmail(),"Your Daily expense:Summary",body);
            }
        });
        log.info("Job ended : sendDailyExpenseSummary");

    }
}
