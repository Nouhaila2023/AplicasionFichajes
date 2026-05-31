package com.example.aplicasionfichajes.servecies;

import com.example.aplicasionfichajes.dtos.EmpleadoCreateDto;
import com.example.aplicasionfichajes.dtos.EmpleadoDto;
import com.example.aplicasionfichajes.dtos.FichajeDto;
import com.example.aplicasionfichajes.entities.Empleado;
import com.example.aplicasionfichajes.entities.Empresa;
import com.example.aplicasionfichajes.exception.BusinessException;
import com.example.aplicasionfichajes.exception.ResourceNotFoundException;
import com.example.aplicasionfichajes.mappers.EmpleadoMapper;
import com.example.aplicasionfichajes.mappers.FichajeMapper;
import com.example.aplicasionfichajes.repositories.EmpleadoRepository;
import com.example.aplicasionfichajes.repositories.EmpresaRepository;
import com.example.aplicasionfichajes.repositories.FichajeRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class EmpleadoServiceImpl implements EmpleadoService {

    @Autowired
    private EmpleadoRepository empleadoRepository;

    @Autowired
    private EmpresaRepository empresaRepository;

    @Autowired
    private FichajeRepository fichajeRepository;

    @Autowired
    private EmpleadoMapper empleadoMapper;

    @Autowired
    private FichajeMapper fichajeMapper;

    @Override
    @Transactional
    public EmpleadoDto create(EmpleadoCreateDto dto) {
        if (empleadoRepository.existsByEmail(dto.email())) {
            throw new BusinessException("Ya existe un empleado con el email: " + dto.email());
        }

        Empresa empresa = empresaRepository.findById(dto.empresaId())
                .orElseThrow(() -> new ResourceNotFoundException("Empresa no encontrada con id: " + dto.empresaId()));

        Empleado empleado = empleadoMapper.toEntity(dto);
        empresa.addEmpleado(empleado);

        return empleadoMapper.toDto(empleadoRepository.save(empleado));
    }

    @Override
    @Transactional(readOnly = true)
    public List<EmpleadoDto> findAll() {
        return empleadoRepository.findAll().stream()
                .map(empleadoMapper::toDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<EmpleadoDto> findById(Long id) {
        return empleadoRepository.findById(id).map(empleadoMapper::toDto);
    }

    @Override
    @Transactional
    public EmpleadoDto update(Long id, EmpleadoCreateDto dto) {
        Empleado empleado = empleadoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Empleado no encontrado con id: " + id));

        // Si cambia el email, verificar que no exista
        if (!empleado.getEmail().equals(dto.email()) && empleadoRepository.existsByEmail(dto.email())) {
            throw new BusinessException("Ya existe un empleado con el email: " + dto.email());
        }

        // Si cambia de empresa, verificar que exista
        if (!empleado.getEmpresa().getId().equals(dto.empresaId())) {
            Empresa nuevaEmpresa = empresaRepository.findById(dto.empresaId())
                    .orElseThrow(() -> new ResourceNotFoundException("Empresa no encontrada con id: " + dto.empresaId()));
            empleado.getEmpresa().removeEmpleado(empleado);
            nuevaEmpresa.addEmpleado(empleado);
        }

        empleado.setNombre(dto.nombre());
        empleado.setEmail(dto.email());
        empleado.setPuesto(dto.puesto());

        return empleadoMapper.toDto(empleadoRepository.save(empleado));
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Empleado empleado = empleadoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Empleado no encontrado con id: " + id));

        // Regla de negocio: no se puede borrar un empleado con fichajes
        if (!empleado.getFichajes().isEmpty()) {
            throw new BusinessException(
                    "No se puede eliminar al empleado '" + empleado.getNombre() +
                            "' porque tiene " + empleado.getFichajes().size() + " fichaje(s) asociado(s)."
            );
        }

        empleadoRepository.delete(empleado);
    }

    @Override
    @Transactional(readOnly = true)
    public List<FichajeDto> getFichajesByEmpleado(Long empleadoId) {
        if (!empleadoRepository.existsById(empleadoId)) {
            throw new ResourceNotFoundException("Empleado no encontrado con id: " + empleadoId);
        }
        return fichajeRepository.findByEmpleadoIdOrderByFechaHoraAsc(empleadoId).stream()
                .map(fichajeMapper::toDto)
                .toList();
    }
}