package com.example.aplicasionfichajes.mappers;

import com.example.aplicasionfichajes.dtos.EmpleadoCreateDto;
import com.example.aplicasionfichajes.dtos.EmpleadoDto;
import com.example.aplicasionfichajes.entities.Empleado;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


@Mapper(componentModel = "spring")
public interface EmpleadoMapper {

    @Mapping(source = "empresa.id", target = "empresaId")
    @Mapping(source = "empresa.nombre", target = "empresaNombre")
    EmpleadoDto toDto(Empleado empleado);

    // La relación con empresa se asigna en el servicio manualmente
    @Mapping(target = "empresa", ignore = true)
    @Mapping(target = "fichajes", ignore = true)
    @Mapping(target = "id", ignore = true)

    Empleado toEntity(EmpleadoCreateDto dto);
}