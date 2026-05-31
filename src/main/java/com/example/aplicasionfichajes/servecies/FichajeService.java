package com.example.aplicasionfichajes.servecies;

import com.example.aplicasionfichajes.dtos.FichajeCreateDto;
import com.example.aplicasionfichajes.dtos.FichajeDto;
import com.example.aplicasionfichajes.dtos.IncidenciaDto;
import com.example.aplicasionfichajes.dtos.InformeMensualDto;

import java.util.List;
import java.util.Optional;

public interface FichajeService {
    FichajeDto create(FichajeCreateDto dto);
    List<FichajeDto> findAll();
    Optional<FichajeDto> findById(Long id);
    InformeMensualDto getInformeMensual(Long empleadoId, int anio, int mes);
    List<IncidenciaDto> getIncidencias(Long empleadoId);
}