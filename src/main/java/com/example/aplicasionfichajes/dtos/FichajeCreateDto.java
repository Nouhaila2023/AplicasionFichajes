package com.example.aplicasionfichajes.dtos;


import com.example.aplicasionfichajes.entities.TipoFichaje;

public record FichajeCreateDto(
        TipoFichaje tipo,

        Long empleadoId,

        // Geolocalización opcional
        Double latitud,
        Double longitud
) {}