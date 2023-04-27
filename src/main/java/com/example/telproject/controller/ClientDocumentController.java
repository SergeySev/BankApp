package com.example.telproject.controller;

import com.example.telproject.dto.ClientDocumentDTO;
import com.example.telproject.service.ClientDocumentService;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

    /**

     Uploads a client document.
     @param client_id the ID of the client to upload the document for
     @param file the image file to upload
     @throws IllegalStateException if the file is empty or is not an image
     */
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful loaded"),
            @ApiResponse(responseCode = "400", description = "File is not an image"),
    })
    @PostMapping(
            path = "/{client_id}/image/upload",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public void uploadClientDock(@PathVariable("client_id") Long client_id,
                                 @RequestParam("file") MultipartFile file) {
        clientDocumentService.uploadClientDock(client_id, file);
    }

    /**

     Gets a list of client documents for a given client ID.
     @param client_id the ID of the client to get documents for
     @return a list of ClientDocumentDTO objects representing the documents
     */
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful retrieval"),
            @ApiResponse(responseCode = "400", description = "Document doesn't exist"),
    })
    @GetMapping(path = "/document")
    public List<ClientDocumentDTO> getClientDocument(@RequestParam("client_id") Long client_id) {
        return clientDocumentService.getClientDocument(client_id);
    }
}
