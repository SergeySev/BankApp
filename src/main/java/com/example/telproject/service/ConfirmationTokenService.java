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

    /**
     * Saves a confirmation token for the client registration to the database.
     *
     * @param token The confirmation token to be saved.
     */
    public void saveConfirmationToken(ConfirmationToken token) {
        confirmationTokenRepository.save(token);
    }

    /**
     * Retrieves a confirmation token by its token string.
     *
     * @param token The token string of the confirmation token to be retrieved.
     * @return An Optional containing the confirmation token, or empty if not found.
     */
    public Optional<ConfirmationToken> getToken(String token) {
        return confirmationTokenRepository.
                        findByToken(token);
    }

    /**
     * Sets the confirmed timestamp of a confirmation token to the current time if the token was confirmed.
     *
     * @param token The token string of the confirmation token to be updated.
     */
    public void setConfirmedAt(String token) {
        confirmationTokenRepository.updateConfirmedAt(LocalDateTime.now(), token);
    }

    /**
     * Deletes a confirmation token associated with a client from the database if the client has a new token.
     *
     * @param client The client whose confirmation token is to be deleted.
     */
    @Transactional
    public void deleteConfirmationToken(Client client) {
        confirmationTokenRepository.deleteByClient(client);
    }

}
