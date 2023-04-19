package com.example.telproject.service;

import com.example.telproject.dto.ClientDocumentDTO;
import com.example.telproject.entity.Client;
import com.example.telproject.entity.ClientDocument;
import com.example.telproject.mapper.ClientDocumentMapper;
import com.example.telproject.repository.ClientDocumentRepository;
import com.example.telproject.repository.ClientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ClientDocumentService {
    private final ClientDocumentRepository clientDocumentRepository;
    private final ClientRepository clientRepository;
    private final ClientDocumentMapper clientDocumentMapper;

    private byte[] imageToByteArray(MultipartFile file) {
        try {
            return file.getBytes();
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }


    public void uploadClientDock(Long clientId, MultipartFile file) {
        // 1. Check if image is not empty
        if (file.isEmpty()) throw new IllegalStateException("File is empty");
        // 2. file is an image
        if (!(file.getContentType().endsWith("jpg")
                || file.getContentType().endsWith("png")
                || file.getContentType().endsWith("jpeg"))) throw new IllegalStateException("File is not image");
        // 3. Check if the client exists in the database
        Client client = clientRepository.findById(clientId).orElseThrow(() -> new IllegalStateException("Client not found"));
        // 4. Store the image in s3 and update database with s3 image link
        byte[] image = imageToByteArray(file);
        ClientDocument clientDocument = new ClientDocument(client, image, file.getContentType().substring(file.getContentType().indexOf("/") + 1));
        clientDocumentRepository.save(clientDocument);
    }

    public List<ClientDocumentDTO> getClientDocument(Long clientId) {
        List<ClientDocument> clientDocument = clientDocumentRepository
                .findAllByClientId(clientId);
        if (clientDocument.isEmpty()) throw new IllegalStateException("Document not found");
        return clientDocumentMapper.toDTOList(clientDocument);
    }
}
