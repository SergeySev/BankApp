package com.example.telproject.service;

import com.example.telproject.dto.CardDTO;
import com.example.telproject.entity.Card;
import com.example.telproject.entity.Client;
import com.example.telproject.entity.enums.CurrencyType;
import com.example.telproject.mapper.AccountMapper;
import com.example.telproject.mapper.AccountMapperImpl;
import com.example.telproject.repository.CardRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AccountServiceTest {

    CardRepository cardRepository = Mockito.mock(CardRepository.class);
    AccountMapper accountMapper = new AccountMapperImpl();
    CardService cardService = new CardService(cardRepository, accountMapper);

    List<CardDTO> createAccountsDTO() {
        List<CardDTO> cardDTOList = new ArrayList<>(2);

        Client client = Mockito.mock(Client.class);

        Card card1 = new Card(client, CurrencyType.EUR);
        Card card2 = new Card(client, CurrencyType.UAH);

        card1.setCard_number("0000000000000000");
        card1.setCsv("000");

        card2.setCard_number("0000000000000000");
        card2.setCsv("000");

        cardDTOList.add(accountMapper.toDto(card1));
        cardDTOList.add(accountMapper.toDto(card2));
        return cardDTOList;
    }

    List<Card> createAccounts() {
        List<Card> cardList = new ArrayList<>(2);

        Client client = Mockito.mock(Client.class);

        Card card1 = new Card(client, CurrencyType.EUR);
        Card card2 = new Card(client, CurrencyType.UAH);

        card1.setCard_number("0000000000000000");
        card1.setCsv("000");

        card2.setCard_number("0000000000000000");
        card2.setCsv("000");

        cardList.add(card1);
        cardList.add(card2);
        return cardList;
    }

    @Test
    void getAccountByProductId() {
        List<CardDTO> expected = createAccountsDTO();
        List<Card> cardList = createAccounts();

        Mockito.when(cardRepository.findByProductId(1L)).thenReturn(cardList);
        List<CardDTO> result = cardService.getAccountByProductId(1L);

        Mockito.verify(cardRepository, Mockito.times(1)).findByProductId(1L);
        assertEquals(expected, result);
    }
}