package com.example.telproject.service;

import com.example.telproject.email.EmailSender;
import com.example.telproject.entity.Client;
import com.example.telproject.mapper.ClientMapper;
import com.example.telproject.mapper.ClientMapperImpl;
import com.example.telproject.repository.ClientRepository;
import com.example.telproject.repository.ManagerRepository;
import com.example.telproject.security.CheckingEmail;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class ClientServiceTest {

    ClientRepository clientRepository = Mockito.mock(ClientRepository.class);
    ClientMapper clientMapper = new ClientMapperImpl();
    EmailSender emailSender = Mockito.mock(EmailSender.class);
    BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
    CheckingEmail checkingEmail = new CheckingEmail();
    ConfirmationTokenService confirmationTokenService = Mockito.mock(ConfirmationTokenService.class);
    ManagerRepository managerRepository = Mockito.mock(ManagerRepository.class);

    ClientService clientService = new ClientService(clientRepository, clientMapper, emailSender, bCryptPasswordEncoder, checkingEmail, confirmationTokenService, managerRepository);

    Client clientFirst = Mockito.mock(Client.class);
    Client clientSecond = Mockito.mock(Client.class);

    @Test
    public void loadUserByUsernameTest(){
        String email = "client@gmail.com";
        Mockito.when(clientRepository.findByEmail(email)).thenReturn(Optional.ofNullable(clientFirst));

        clientService.loadUserByUsername(email);
        Mockito.verify(clientRepository, Mockito.times(1)).findByEmail(email);
    }

    @Test
    public void loadUserByUsernameException(){
        UsernameNotFoundException usernameNotFoundException = assertThrows(
                UsernameNotFoundException.class,
                () -> clientService.loadUserByUsername("email"));
        assertEquals("User with email not found", usernameNotFoundException.getMessage());
        Mockito.verify(clientRepository, Mockito.times(1)).findByEmail("email");
    }
}