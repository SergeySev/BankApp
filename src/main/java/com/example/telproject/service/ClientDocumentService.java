package com.example.telproject.service;

import com.example.telproject.repository.ClientDocumentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class ClientDocumentService {
    private final ClientDocumentRepository clientDocumentRepository;


    public void uploadClientDock(Long clientId, MultipartFile file) {
        // 1. Check if image is not empty
        // 2. file is an image
        // 3. The client exists in our database
        // 4. Grab some metadata from file if any
        // 5. Store the image in s3 and update database with s3 image link
    }
}
