package com.example.aplicasionfichajes.repositories;


import com.example.aplicasionfichajes.entities.Empleado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmpleadoRepository extends JpaRepository<Empleado, Long> {
    List<Empleado> findByEmpresaId(Long empresaId);
    boolean existsByEmail(String email);
}