package com.example.aplicasionfichajes.controllers;

import com.example.aplicasionfichajes.dtos.FichajeCreateDto;
import com.example.aplicasionfichajes.dtos.FichajeDto;
import com.example.aplicasionfichajes.servecies.FichajeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/fichajes")
public class FichajeController {

    @Autowired
    private FichajeService fichajeService;

    @PostMapping
    public ResponseEntity<FichajeDto> registrarFichaje(@RequestBody FichajeCreateDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(fichajeService.create(dto));
    }

    @GetMapping
    public ResponseEntity<List<FichajeDto>> getAllFichajes() {
        return ResponseEntity.ok(fichajeService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<FichajeDto> getFichaje(@PathVariable Long id) {
        return fichajeService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}