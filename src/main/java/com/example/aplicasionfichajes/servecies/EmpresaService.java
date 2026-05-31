package com.example.aplicasionfichajes.servecies;

import com.example.aplicasionfichajes.dtos.EmpleadoDto;
import com.example.aplicasionfichajes.dtos.EmpresaCreateDto;
import com.example.aplicasionfichajes.dtos.EmpresaDto;

import java.util.List;
import java.util.Optional;

public interface EmpresaService {
    EmpresaDto create(EmpresaCreateDto dto);
    List<EmpresaDto> findAll();
    Optional<EmpresaDto> findById(Long id);
    EmpresaDto update(Long id, EmpresaCreateDto dto);
    void delete(Long id);
    List<EmpleadoDto> getEmpleadosByEmpresa(Long empresaId);
}