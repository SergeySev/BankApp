package com.example.telproject.service;

import com.example.telproject.dto.ConfirmationTokenDTO;
import com.example.telproject.entity.ConfirmationToken;
import com.example.telproject.mapper.ConfirmationTokenMapper;
import com.example.telproject.repository.ConfirmationTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ConfirmationTokenService {
    private final ConfirmationTokenRepository confirmationTokenRepository;
    private final ConfirmationTokenMapper confirmationTokenMapper;

    public void saveConfirmationToken(ConfirmationToken token) {
        System.out.println("\n\n\n\n TOKEN + " + token + "\n\n\n");
        confirmationTokenRepository.save(token);
    }

    public Optional<ConfirmationToken> getToken(String token) {
        return confirmationTokenRepository.
                        findByToken(token);
    }

    public int setConfirmedAt(String token) {
        return confirmationTokenRepository.updateConfirmedAt(LocalDateTime.now(), token);
    }

    public ConfirmationTokenDTO getConfirmationTokenByClientId(Long id) {
        return confirmationTokenMapper.toDto(confirmationTokenRepository.findByClientId(id).orElseThrow(() -> new IllegalStateException("Token not found")));
    }

    public void deleteConfirmationToken(Long tokenId) {
        confirmationTokenRepository.deleteById(tokenId);
    }

}
