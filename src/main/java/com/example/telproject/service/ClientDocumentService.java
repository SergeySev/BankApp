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

    /**

     Converts a MultipartFile object representing an image file into a byte array.
     @param file the MultipartFile object representing the image file
     @return a byte array representing the image
     @throws IllegalStateException if an exception occurs while converting the file into a byte array
     */
    private byte[] imageToByteArray(MultipartFile file) {
        try {
            return file.getBytes();
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }


    /**

     Uploads a document for a client and stores it in the database. The document is associated with the given client by their ID.
     <p>
     The uploaded document must be an image in JPEG, JPG, or PNG format, otherwise an IllegalStateException is thrown.
     <p>
     If the client with the given ID is not found in the database, an IllegalStateException is thrown.
     @param clientId the ID of the client the document is associated with
     @param file the uploaded file containing the document to store
     @throws IllegalStateException if the file is empty, not an image, or if the client with the given ID is not found
     */
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

    /**

     Retrieves a list of document associated with a client by their ID from the database.
     <p>
     If no documents are found for the given client ID, an IllegalStateException is thrown.
     @param clientId the ID of the client to retrieve the documents for
     @return a list of ClientDocumentDTO objects representing the retrieved documents
     @throws IllegalStateException if no documents are found for the given client ID
     */
    public List<ClientDocumentDTO> getClientDocument(Long clientId) {
        List<ClientDocument> clientDocument = clientDocumentRepository
                .findAllByClientId(clientId);
        if (clientDocument.isEmpty()) throw new IllegalStateException("Document not found");
        return clientDocumentMapper.toDTOList(clientDocument);
    }
}
