package com.example.telproject.mapper;

import com.example.telproject.dto.ClientDTO;
import com.example.telproject.entity.Client;
import com.example.telproject.entity.Manager;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ClientMapper {
    ClientDTO toDto(Client client);

    default String map(Manager manager) {
        return manager.toString();
    }
}
