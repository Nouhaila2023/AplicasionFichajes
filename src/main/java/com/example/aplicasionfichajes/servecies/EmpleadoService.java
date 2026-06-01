package com.example.aplicasionfichajes.servecies;

import com.example.aplicasionfichajes.dtos.EmpleadoCreateDto;
import com.example.aplicasionfichajes.dtos.EmpleadoDto;
import com.example.aplicasionfichajes.dtos.FichajeDto;

import java.util.List;
import java.util.Optional;

public interface EmpleadoService {
    //metodos
    EmpleadoDto create(EmpleadoCreateDto dto);
    List<EmpleadoDto> findAll();
    Optional<EmpleadoDto> findById(Long id);
    EmpleadoDto update(Long id, EmpleadoCreateDto dto);
    void delete(Long id);
    List<FichajeDto> getFichajesByEmpleado(Long empleadoId);
}