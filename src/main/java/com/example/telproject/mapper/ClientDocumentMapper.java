package com.example.telproject.mapper;

import com.example.telproject.dto.ClientDocumentDTO;
import com.example.telproject.entity.ClientDocument;
import org.mapstruct.Mapper;

import java.util.List;
@Mapper(componentModel = "spring")
public interface ClientDocumentMapper {

    ClientDocumentDTO toDTO(ClientDocument clientDocument);

    List<ClientDocumentDTO> toDTO(List<ClientDocument> clientDocument);
}
