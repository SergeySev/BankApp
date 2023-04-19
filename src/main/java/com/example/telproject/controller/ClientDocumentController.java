package com.example.telproject.controller;

import com.example.telproject.dto.ClientDocumentDTO;
import com.example.telproject.service.ClientDocumentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/client-document")
@CrossOrigin("http://localhost:3000/")
public class ClientDocumentController {
    private final ClientDocumentService clientDocumentService;

    @PostMapping(
            path = "/{client_id}/image/upload",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public void uploadClientDock(@PathVariable("client_id") Long client_id,
                                 @RequestParam("file") MultipartFile file) {
        clientDocumentService.uploadClientDock(client_id, file);
    }

    @GetMapping(path = "/document")
    public List<ClientDocumentDTO> getClientDocument(@RequestParam("client_id") Long client_id) {
        return clientDocumentService.getClientDocument(client_id);
    }
}
