package com.example.aplicasionfichajes.servecies;

import com.example.aplicasionfichajes.dtos.FichajeCreateDto;
import com.example.aplicasionfichajes.dtos.FichajeDto;
import com.example.aplicasionfichajes.dtos.IncidenciaDto;
import com.example.aplicasionfichajes.dtos.InformeMensualDto;
import com.example.aplicasionfichajes.entities.Empleado;
import com.example.aplicasionfichajes.entities.Fichaje;
import com.example.aplicasionfichajes.entities.TipoFichaje;
import com.example.aplicasionfichajes.exception.ResourceNotFoundException;
import com.example.aplicasionfichajes.mappers.FichajeMapper;
import com.example.aplicasionfichajes.repositories.EmpleadoRepository;
import com.example.aplicasionfichajes.repositories.FichajeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class FichajeServiceImpl implements FichajeService {

    // Umbral de horas extra: más de 10h en un día se considera incidencia
    private static final int UMBRAL_HORAS_EXTRA = 10;

    //accede a tabla
    @Autowired
    private FichajeRepository fichajeRepository;

    @Autowired
    private EmpleadoRepository empleadoRepository;

    @Autowired
    private FichajeMapper fichajeMapper;

    @Override
    @Transactional
    public FichajeDto create(FichajeCreateDto dto) {
        Empleado empleado = empleadoRepository.findById(dto.empleadoId())
                .orElseThrow(() -> new ResourceNotFoundException("Empleado no encontrado con id: " + dto.empleadoId()));

        Fichaje fichaje = fichajeMapper.toEntity(dto);
        fichaje.setFechaHora(LocalDateTime.now());
        empleado.addFichaje(fichaje);

        return fichajeMapper.toDto(fichajeRepository.save(fichaje));
    }

    @Override
    @Transactional(readOnly = true)
    public List<FichajeDto> findAll() {
        return fichajeRepository.findAll().stream()
                .map(fichajeMapper::toDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<FichajeDto> findById(Long id) {
        return fichajeRepository.findById(id).map(fichajeMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public InformeMensualDto getInformeMensual(Long empleadoId, int anio, int mes) {
        Empleado empleado = empleadoRepository.findById(empleadoId)
                .orElseThrow(() -> new ResourceNotFoundException("Empleado no encontrado con id: " + empleadoId));

        YearMonth yearMonth = YearMonth.of(anio, mes);
        LocalDateTime inicio = yearMonth.atDay(1).atStartOfDay();
        LocalDateTime fin = yearMonth.atEndOfMonth().atTime(23, 59, 59);

        List<Fichaje> fichajes = fichajeRepository
                .findByEmpleadoIdAndFechaHoraBetweenOrderByFechaHoraAsc(empleadoId, inicio, fin);

        // Calcular tiempo trabajado y detectar incidencias
        long totalMinutos = 0;
        int jornadasCompletas = 0;
        List<String> incidencias = new ArrayList<>();

        List<Fichaje> entradas = fichajes.stream()
                .filter(f -> f.getTipo() == TipoFichaje.ENTRADA).toList();
        List<Fichaje> salidas = fichajes.stream()
                .filter(f -> f.getTipo() == TipoFichaje.SALIDA).toList();

        // Emparejar secuencialmente ENTRADA → SALIDA
        int i = 0; // índice entradas
        int j = 0; // índice salidas

        while (i < entradas.size()) {
            Fichaje entrada = entradas.get(i);

            // Buscar la siguiente SALIDA posterior a esta ENTRADA
            Fichaje salidaEmparejada = null;
            while (j < salidas.size()) {
                if (salidas.get(j).getFechaHora().isAfter(entrada.getFechaHora())) {
                    salidaEmparejada = salidas.get(j);
                    j++;
                    break;
                }
                // Salida sin entrada previa
                incidencias.add("SALIDA_SIN_ENTRADA: Salida registrada el " +
                        salidas.get(j).getFechaHora() + " sin entrada previa.");
                j++;
            }

            if (salidaEmparejada != null) {
                long minutosJornada = Duration.between(entrada.getFechaHora(), salidaEmparejada.getFechaHora()).toMinutes();
                totalMinutos += minutosJornada;
                jornadasCompletas++;

                // Detectar horas extra
                if (minutosJornada > UMBRAL_HORAS_EXTRA * 60) {
                    incidencias.add("HORAS_EXTRA: El " + entrada.getFechaHora().toLocalDate() +
                            " se trabajaron " + (minutosJornada / 60) + "h " + (minutosJornada % 60) + "min (supera el umbral de " + UMBRAL_HORAS_EXTRA + "h).");
                }
            } else {
                // Entrada sin salida
                incidencias.add("ENTRADA_SIN_SALIDA: Entrada registrada el " +
                        entrada.getFechaHora() + " sin salida posterior.");
            }
            i++;
        }

        // Salidas restantes sin entrada
        while (j < salidas.size()) {
            incidencias.add("SALIDA_SIN_ENTRADA: Salida registrada el " +
                    salidas.get(j).getFechaHora() + " sin entrada previa.");
            j++;
        }

        return new InformeMensualDto(
                empleadoId,
                empleado.getNombre(),
                anio,
                mes,
                totalMinutos,
                totalMinutos / 60,
                totalMinutos % 60,
                jornadasCompletas,
                incidencias
        );
    }

    @Override
    @Transactional(readOnly = true)
    public List<IncidenciaDto> getIncidencias(Long empleadoId) {
        Empleado empleado = empleadoRepository.findById(empleadoId)
                .orElseThrow(() -> new ResourceNotFoundException("Empleado no encontrado con id: " + empleadoId));

        List<Fichaje> fichajes = fichajeRepository.findByEmpleadoIdOrderByFechaHoraAsc(empleadoId);
        List<IncidenciaDto> incidencias = new ArrayList<>();

        List<Fichaje> entradas = fichajes.stream()
                .filter(f -> f.getTipo() == TipoFichaje.ENTRADA).toList();
        List<Fichaje> salidas = fichajes.stream()
                .filter(f -> f.getTipo() == TipoFichaje.SALIDA).toList();

        int i = 0;
        int j = 0;

        while (i < entradas.size()) {
            Fichaje entrada = entradas.get(i);
            Fichaje salidaEmparejada = null;

            while (j < salidas.size()) {
                if (salidas.get(j).getFechaHora().isAfter(entrada.getFechaHora())) {
                    salidaEmparejada = salidas.get(j);
                    j++;
                    break;
                }
                incidencias.add(new IncidenciaDto(
                        "SALIDA_SIN_ENTRADA",
                        "Salida sin entrada previa coherente",
                        salidas.get(j).getFechaHora()
                ));
                j++;
            }

            if (salidaEmparejada != null) {
                long minutos = Duration.between(entrada.getFechaHora(), salidaEmparejada.getFechaHora()).toMinutes();
                if (minutos > UMBRAL_HORAS_EXTRA * 60) {
                    incidencias.add(new IncidenciaDto(
                            "HORAS_EXTRA",
                            "Jornada de " + (minutos / 60) + "h " + (minutos % 60) + "min supera el umbral de " + UMBRAL_HORAS_EXTRA + "h",
                            entrada.getFechaHora()
                    ));
                }
            } else {
                incidencias.add(new IncidenciaDto(
                        "ENTRADA_SIN_SALIDA",
                        "Entrada sin salida registrada",
                        entrada.getFechaHora()
                ));
            }
            i++;
        }

        while (j < salidas.size()) {
            incidencias.add(new IncidenciaDto(
                    "SALIDA_SIN_ENTRADA",
                    "Salida sin entrada previa coherente",
                    salidas.get(j).getFechaHora()
            ));
            j++;
        }

        return incidencias;
    }
}