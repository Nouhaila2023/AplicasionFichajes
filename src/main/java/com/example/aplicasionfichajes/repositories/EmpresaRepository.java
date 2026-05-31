package com.example.aplicasionfichajes.repositories;


import com.example.aplicasionfichajes.entities.Empresa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmpresaRepository extends JpaRepository<Empresa, Long> {
    boolean existsByCif(String cif);
}