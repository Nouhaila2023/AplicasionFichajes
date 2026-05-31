package com.example.aplicasionfichajes.repositories;

import com.example.aplicasionfichajes.entities.Fichaje;
import com.example.aplicasionfichajes.entities.TipoFichaje;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface FichajeRepository extends JpaRepository<Fichaje, Long> {

    List<Fichaje> findByEmpleadoIdOrderByFechaHoraAsc(Long empleadoId);

    List<Fichaje> findByEmpleadoIdAndFechaHoraBetweenOrderByFechaHoraAsc(
            Long empleadoId,
            LocalDateTime inicio,
            LocalDateTime fin
    );

    boolean existsByEmpleadoIdAndTipoAndFechaHoraBetween(
            Long empleadoId,
            TipoFichaje tipo,
            LocalDateTime inicio,
            LocalDateTime fin
    );
}