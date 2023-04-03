package com.example.telproject.mapper;

import com.example.telproject.dto.ManagerDTO;
import com.example.telproject.entity.Manager;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ManagerMapper {
    ManagerDTO toDto(Manager manager);
    List<ManagerDTO> listToDTO(List<Manager> managers);

}
