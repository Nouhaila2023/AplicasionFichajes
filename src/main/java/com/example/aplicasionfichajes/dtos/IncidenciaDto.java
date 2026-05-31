package com.example.aplicasionfichajes.dtos;

import java.time.LocalDateTime;

public record IncidenciaDto(
        String tipo,
        String descripcion,
        LocalDateTime fechaHora
) {}