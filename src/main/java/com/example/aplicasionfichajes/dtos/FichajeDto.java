package com.example.aplicasionfichajes.dtos;

import com.example.aplicasionfichajes.entities.TipoFichaje;

import java.time.LocalDateTime;

public record FichajeDto(
        Long id,
        LocalDateTime fechaHora,
        TipoFichaje tipo,
        Double latitud,
        Double longitud,
        Long empleadoId,
        String empleadoNombre
) {}