package com.example.telproject.mapper;

import com.example.telproject.dto.ConfirmationTokenDTO;
import com.example.telproject.entity.Client;
import com.example.telproject.entity.ConfirmationToken;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ConfirmationTokenMapper {
    ConfirmationTokenDTO toDto(ConfirmationToken token);

    default String map(Client client) {
        return client.toString();
    }

}
