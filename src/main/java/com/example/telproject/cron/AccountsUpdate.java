package com.example.telproject.cron;

import com.example.telproject.TelProjectApplication;
import com.example.telproject.entity.Account;
import com.example.telproject.entity.enums.AccountStatus;
import com.example.telproject.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AccountsUpdate {

    private final AccountRepository accountRepository;

    @Scheduled(cron = "0 0 0 * * *")
    public void updateActiveAccount() {
        List<Account> accounts = accountRepository.findAllActive();
        for (Account account : accounts) {
            if (account.getExpired_at().before(Timestamp.valueOf(LocalDateTime.now()))) {
                account.setStatus(AccountStatus.BLOCKED);
            }
        }
        accountRepository.saveAll(accounts);
        LoggerFactory.getLogger(TelProjectApplication.class).info("All accounts is up to date");
    }

}
