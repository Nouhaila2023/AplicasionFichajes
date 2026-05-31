package com.example.aplicasionfichajes.dtos;
import java.util.List;

public record InformeMensualDto(
        Long empleadoId,
        String empleadoNombre,
        int anio,
        int mes,
        long totalMinutosTrabajados,
        long horasTrabajadas,
        long minutosTrabajados,
        int jornadasCompletas,
        List<String> incidencias
) {}