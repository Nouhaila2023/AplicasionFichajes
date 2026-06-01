package com.example.aplicasionfichajes.servecies;


import com.example.aplicasionfichajes.dtos.EmpleadoDto;
import com.example.aplicasionfichajes.dtos.EmpresaCreateDto;
import com.example.aplicasionfichajes.dtos.EmpresaDto;
import com.example.aplicasionfichajes.entities.Empresa;
import com.example.aplicasionfichajes.exception.BusinessException;
import com.example.aplicasionfichajes.exception.ResourceNotFoundException;
import com.example.aplicasionfichajes.mappers.EmpleadoMapper;
import com.example.aplicasionfichajes.mappers.EmpresaMapper;
import com.example.aplicasionfichajes.repositories.EmpleadoRepository;
import com.example.aplicasionfichajes.repositories.EmpresaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class EmpresaServiceImpl implements EmpresaService {

    //accede a tabla
    @Autowired
    private EmpresaRepository empresaRepository;

    @Autowired
    private EmpleadoRepository empleadoRepository;

    @Autowired
    private EmpresaMapper empresaMapper;

    @Autowired
    private EmpleadoMapper empleadoMapper;

    @Override
    @Transactional
    public EmpresaDto create(EmpresaCreateDto dto) {
        if (empresaRepository.existsByCif(dto.cif())) {
            throw new BusinessException("Ya existe una empresa con el CIF: " + dto.cif());
        }
        Empresa empresa = empresaMapper.toEntity(dto);
        return empresaMapper.toDto(empresaRepository.save(empresa));
    }
    //Pide todas las empresas
    @Override
    @Transactional(readOnly = true)
    public List<EmpresaDto> findAll() {
        return empresaRepository.findAll().stream()
                .map(empresaMapper::toDto)
                .toList();
    }


    @Override
    @Transactional(readOnly = true)
    public Optional<EmpresaDto> findById(Long id) {
        return empresaRepository.findById(id).map(empresaMapper::toDto);
    }

    @Override
    @Transactional
    public EmpresaDto update(Long id, EmpresaCreateDto dto) {
        Empresa empresa = empresaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Empresa no encontrada con id: " + id));

        // Si cambia el CIF, verificar que no exista
        if (!empresa.getCif().equals(dto.cif()) && empresaRepository.existsByCif(dto.cif())) {
            throw new BusinessException("Ya existe una empresa con el CIF: " + dto.cif());
        }

        empresa.setNombre(dto.nombre());
        empresa.setCif(dto.cif());
        empresa.setDireccion(dto.direccion());

        return empresaMapper.toDto(empresaRepository.save(empresa));
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Empresa empresa = empresaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Empresa no encontrada con id: " + id));

        // Regla de negocio: no se puede borrar una empresa con empleados
        if (!empresa.getEmpleados().isEmpty()) {
            throw new BusinessException(
                    "No se puede eliminar la empresa '" + empresa.getNombre() +
                            "' porque tiene " + empresa.getEmpleados().size() + " empleado(s) asociado(s)."
            );
        }

        empresaRepository.delete(empresa);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EmpleadoDto> getEmpleadosByEmpresa(Long empresaId) {
        if (!empresaRepository.existsById(empresaId)) {
            throw new ResourceNotFoundException("Empresa no encontrada con id: " + empresaId);
        }
        return empleadoRepository.findByEmpresaId(empresaId).stream()
                .map(empleadoMapper::toDto)
                .toList();
    }
}