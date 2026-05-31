package com.example.aplicasionfichajes.entities;


import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity(name = "empresas")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString(exclude = "empleados")
public class Empresa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;

    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false, unique = true)
    private String cif;

    private String direccion;

    @OneToMany(mappedBy = "empresa", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Empleado> empleados = new ArrayList<>();

    public void addEmpleado(Empleado empleado) {
        empleados.add(empleado);
        empleado.setEmpresa(this);
    }

    public void removeEmpleado(Empleado empleado) {
        empleados.remove(empleado);
        empleado.setEmpresa(null);
    }
}