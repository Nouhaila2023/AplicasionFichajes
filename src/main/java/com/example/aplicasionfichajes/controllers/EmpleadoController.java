package com.example.aplicasionfichajes.controllers;
import com.example.aplicasionfichajes.dtos.*;
import com.example.aplicasionfichajes.servecies.EmpleadoService;
import com.example.aplicasionfichajes.servecies.FichajeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/empleados")
public class EmpleadoController {

    @Autowired
    private EmpleadoService empleadoService;

    @Autowired
    private FichajeService fichajeService;

    @GetMapping
    public ResponseEntity<List<EmpleadoDto>> getAllEmpleados() {
        return ResponseEntity.ok(empleadoService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<EmpleadoDto> getEmpleado(@PathVariable Long id) {
        return empleadoService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<EmpleadoDto> createEmpleado(@RequestBody EmpleadoCreateDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(empleadoService.create(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<EmpleadoDto> updateEmpleado(@PathVariable Long id,
                                                      @RequestBody EmpleadoCreateDto dto) {
        return ResponseEntity.ok(empleadoService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmpleado(@PathVariable Long id) {
        empleadoService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/fichajes")
    public ResponseEntity<List<FichajeDto>> getFichajesByEmpleado(@PathVariable Long id) {
        return ResponseEntity.ok(empleadoService.getFichajesByEmpleado(id));
    }

    @GetMapping("/{id}/informe-mensual")
    public ResponseEntity<InformeMensualDto> getInformeMensual(
            @PathVariable Long id,
            @RequestParam int anio,
            @RequestParam int mes) {
        return ResponseEntity.ok(fichajeService.getInformeMensual(id, anio, mes));
    }

    @GetMapping("/{id}/incidencias")
    public ResponseEntity<List<IncidenciaDto>> getIncidencias(@PathVariable Long id) {
        return ResponseEntity.ok(fichajeService.getIncidencias(id));
    }
}