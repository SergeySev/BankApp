package com.example.telproject.service;

import com.example.telproject.dto.CardDTO;
import com.example.telproject.entity.Card;
import com.example.telproject.mapper.AccountMapper;
import com.example.telproject.repository.CardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CardService {

    private final CardRepository cardRepository;
    private final AccountMapper accountMapper;
    private final String ACCOUNT_NOT_FOUND = "Account with %s: %s doesn't exist in the DataBase";


    /**
     * Retrieves a list of accounts with the given productId.
     * <p>
     * If no accounts with the provided productId are found, a RuntimeException is thrown.
     * @param productId the productId to search for
     * @return a List of AccountDTO objects representing the accounts found
     * @throws RuntimeException if no accounts with the provided productId are found
     */
    public List<CardDTO> getAccountByProductId(Long productId) {
        List<Card> cards = cardRepository.findByProductId(productId);
        if (cards.isEmpty()) {
            throw new RuntimeException(String.format(ACCOUNT_NOT_FOUND, "Product id", productId));
        }
        return accountMapper.toDtoList(cards);
    }


}
