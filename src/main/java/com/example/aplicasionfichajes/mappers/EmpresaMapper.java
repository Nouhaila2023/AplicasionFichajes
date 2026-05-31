package com.example.aplicasionfichajes.mappers;

import com.example.aplicasionfichajes.dtos.EmpresaCreateDto;
import com.example.aplicasionfichajes.dtos.EmpresaDto;
import com.example.aplicasionfichajes.entities.Empresa;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface EmpresaMapper {
    EmpresaDto toDto(Empresa empresa);
    Empresa toEntity(EmpresaCreateDto dto);
}