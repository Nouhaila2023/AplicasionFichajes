package com.example.aplicasionfichajes.dtos;

public record EmpleadoDto(
        Long id,
        String nombre,
        String email,
        String puesto,
        Long empresaId,
        String empresaNombre
) {}