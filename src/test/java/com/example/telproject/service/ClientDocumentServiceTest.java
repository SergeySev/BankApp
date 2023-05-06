package com.example.telproject.service;

import com.example.telproject.dto.ClientDocumentDTO;
import com.example.telproject.entity.Client;
import com.example.telproject.entity.ClientDocument;
import com.example.telproject.mapper.ClientDocumentMapper;
import com.example.telproject.mapper.ClientDocumentMapperImpl;
import com.example.telproject.repository.ClientDocumentRepository;
import com.example.telproject.repository.ClientRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class ClientDocumentServiceTest {

    ClientDocumentRepository clientDocumentRepository = Mockito.mock(ClientDocumentRepository.class);
    ClientRepository clientRepository = Mockito.mock(ClientRepository.class);
    ClientDocumentMapper clientDocumentMapper = new ClientDocumentMapperImpl();
    ClientDocumentService clientDocumentService = new ClientDocumentService(clientDocumentRepository, clientRepository, clientDocumentMapper);

    Client client = Mockito.mock(Client.class);

    MultipartFile jpg = new MockMultipartFile("file", "filename.jpg", "image/jpg", "some xml".getBytes());
    MultipartFile png = new MockMultipartFile("file", "filename.png", "image/png", "some xml".getBytes());
    MultipartFile jpeg = new MockMultipartFile("file", "filename.jpeg", "image/jpeg", "some xml".getBytes());
    MultipartFile empty = new MockMultipartFile("file", "filename.jpeg", "image/jpeg", new byte[0]);
    MultipartFile pdf = new MockMultipartFile("file", "filename.pdf", "document/pdf", "some xml".getBytes());

    List<ClientDocument> clientDocuments = List.of(
            new ClientDocument(client, "some xml".getBytes(), "jpeg"),
            new ClientDocument(client, "some xml".getBytes(), "png"));

    @Test
    public void testUploadClientDockJPG() {
        Mockito.when(clientRepository.findById(1L)).thenReturn(Optional.ofNullable(client));
        clientDocumentService.uploadClientDock(1L, jpg);
        Mockito.verify(clientDocumentRepository, Mockito.times(1)).save(Mockito.any());
    }

    @Test
    public void testUploadClientDockPNG() {
        Mockito.when(clientRepository.findById(1L)).thenReturn(Optional.ofNullable(client));
        clientDocumentService.uploadClientDock(1L, png);
        Mockito.verify(clientDocumentRepository, Mockito.times(1)).save(Mockito.any());
    }

    @Test
    public void testUploadClientDockJPEG() {
        Mockito.when(clientRepository.findById(1L)).thenReturn(Optional.ofNullable(client));
        clientDocumentService.uploadClientDock(1L, jpeg);
        Mockito.verify(clientDocumentRepository, Mockito.times(1)).save(Mockito.any());
    }

    @Test
    public void testUploadClientDockPDF() {
        IllegalStateException illegalStateException = assertThrows(IllegalStateException.class, () -> clientDocumentService.uploadClientDock(1L, pdf));

        assertEquals("File is not image", illegalStateException.getMessage());
        Mockito.verify(clientDocumentRepository, Mockito.never()).save(Mockito.any());
    }

    @Test
    public void testUploadClientDockEmpty() {
        IllegalStateException illegalStateException = assertThrows(IllegalStateException.class, () -> clientDocumentService.uploadClientDock(1L, empty));

        assertEquals("File is empty", illegalStateException.getMessage());
        Mockito.verify(clientDocumentRepository, Mockito.never()).save(Mockito.any());
    }

    @Test()
    public void testClientNotFound() {
        IllegalStateException illegalStateException = assertThrows(IllegalStateException.class, () -> clientDocumentService.uploadClientDock(1L, jpg));
        assertEquals("Client not found", illegalStateException.getMessage());
        Mockito.verify(clientDocumentRepository, Mockito.never()).save(Mockito.any());
    }

    @Test
    void getClientDock() {
        Mockito.when(clientDocumentRepository.findAllByClientId(1L)).thenReturn(clientDocuments);
        List<ClientDocumentDTO> clientDocumentDTOS = clientDocumentMapper.toDTOList(clientDocuments);

        assertEquals(clientDocumentDTOS, clientDocumentService.getClientDocument(1L));
        Mockito.verify(clientDocumentRepository, Mockito.times(1)).findAllByClientId(1L);
    }

    @Test
    void getClientDocumentException() {
        IllegalStateException illegalStateException = assertThrows(IllegalStateException.class, () -> clientDocumentService.getClientDocument(1L));
        assertEquals("Document not found", illegalStateException.getMessage());
        Mockito.verify(clientDocumentRepository, Mockito.times(1)).findAllByClientId(1L);
    }
}