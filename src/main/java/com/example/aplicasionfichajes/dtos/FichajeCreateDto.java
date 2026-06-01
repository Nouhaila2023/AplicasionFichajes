package com.example.aplicasionfichajes.dtos;


import com.example.aplicasionfichajes.entities.TipoFichaje;

public record FichajeCreateDto(
        TipoFichaje tipo,

        Long empleadoId,

        Double latitud,
        Double longitud
) {}