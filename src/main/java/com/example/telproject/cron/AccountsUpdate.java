package com.example.telproject.cron;

import com.example.telproject.TelProjectApplication;
import com.example.telproject.entity.Card;
import com.example.telproject.entity.enums.AccountStatus;
import com.example.telproject.repository.CardRepository;
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

    private final CardRepository cardRepository;

    @Scheduled(cron = "0 0 0 * * *")
    public void updateActiveAccount() {
        List<Card> cards = cardRepository.findAllActive();
        for (Card card : cards) {
            if (card.getExpired_at().before(Timestamp.valueOf(LocalDateTime.now()))) {
                card.setStatus(AccountStatus.BLOCKED);
            }
        }
        cardRepository.saveAll(cards);
        LoggerFactory.getLogger(TelProjectApplication.class).info("All accounts is up to date");
    }

}
