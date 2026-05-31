package com.example.aplicasionfichajes.controllers;

import com.example.aplicasionfichajes.dtos.EmpleadoDto;
import com.example.aplicasionfichajes.dtos.EmpresaCreateDto;
import com.example.aplicasionfichajes.dtos.EmpresaDto;
import com.example.aplicasionfichajes.servecies.EmpresaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/empresas")
public class EmpresaController {

    @Autowired
    private EmpresaService empresaService;

    @GetMapping
    public ResponseEntity<List<EmpresaDto>> getAllEmpresas() {
        return ResponseEntity.ok(empresaService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<EmpresaDto> getEmpresa(@PathVariable Long id) {
        return empresaService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<EmpresaDto> createEmpresa(@RequestBody EmpresaCreateDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(empresaService.create(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<EmpresaDto> updateEmpresa(@PathVariable Long id,
                                                    @RequestBody EmpresaCreateDto dto) {
        return ResponseEntity.ok(empresaService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmpresa(@PathVariable Long id) {
        empresaService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/empleados")
    public ResponseEntity<List<EmpleadoDto>> getEmpleadosByEmpresa(@PathVariable Long id) {
        return ResponseEntity.ok(empresaService.getEmpleadosByEmpresa(id));
    }
}