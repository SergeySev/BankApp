package com.example.telproject.mapper;

import com.example.telproject.dto.ClientDTO;
import com.example.telproject.entity.Client;
import com.example.telproject.entity.Manager;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ClientMapper {
    ClientDTO toDto(Client client);
    List<ClientDTO> listToDTO(List<Client> clients);

    default String map(Manager manager) {
        return manager.toString();
    }
}
