package com.example.aplicasionfichajes.dtos;


public record EmpleadoCreateDto(
        String nombre,
        String email,
        String puesto,
        Long empresaId
) {}