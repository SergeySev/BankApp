package com.example.telproject.service;

import com.example.telproject.entity.Client;
import com.example.telproject.entity.ConfirmationToken;
import com.example.telproject.repository.ConfirmationTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ConfirmationTokenService {
    private final ConfirmationTokenRepository confirmationTokenRepository;

    public void saveConfirmationToken(ConfirmationToken token) {
        confirmationTokenRepository.save(token);
    }

    public Optional<ConfirmationToken> getToken(String token) {
        return confirmationTokenRepository.
                        findByToken(token);
    }

    public void setConfirmedAt(String token) {
        confirmationTokenRepository.updateConfirmedAt(LocalDateTime.now(), token);
    }

    @Transactional
    public void deleteConfirmationToken(Client client) {
        confirmationTokenRepository.deleteByClient(client);
    }

}
