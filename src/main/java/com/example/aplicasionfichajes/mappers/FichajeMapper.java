package com.example.aplicasionfichajes.mappers;

import com.example.aplicasionfichajes.dtos.FichajeCreateDto;
import com.example.aplicasionfichajes.dtos.FichajeDto;
import com.example.aplicasionfichajes.entities.Fichaje;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface FichajeMapper {

    @Mapping(source = "empleado.id", target = "empleadoId")
    @Mapping(source = "empleado.nombre", target = "empleadoNombre")
    FichajeDto toDto(Fichaje fichaje);

    @Mapping(target = "empleado", ignore = true)
    @Mapping(target = "fechaHora", ignore = true)
    @Mapping(target = "id", ignore = true)
    Fichaje toEntity(FichajeCreateDto dto);
}