package com.belmonte.viajes_service.service;

import com.belmonte.viajes_service.model.EstadoViaje;
import com.belmonte.viajes_service.model.Viaje;
import com.belmonte.viajes_service.repository.ViajeRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ViajeService {

    private final ViajeRepository viajeRepository;

    public ViajeService(ViajeRepository viajeRepository) {
        this.viajeRepository = viajeRepository;
    }

    public List<Viaje> obtenerTodos() {
        return viajeRepository.findAll();
    }

    public Optional<Viaje> buscarPorId(Long id) {
        return viajeRepository.findById(id);
    }

    public Viaje crearViaje(Viaje viaje) {
        viaje.setEstado(EstadoViaje.SOLICITADO);
        return viajeRepository.save(viaje);
    }

    public Optional<Viaje> asignarConductorYVehiculo(Long viajeId, String conductor, Long vehiculoId) {
        return viajeRepository.findById(viajeId).map(v -> {
            v.setConductor(conductor);
            v.setVehiculoId(vehiculoId);
            v.setEstado(EstadoViaje.ASIGNADO);
            return viajeRepository.save(v);
        });
    }

    public Optional<Viaje> actualizarEstado(Long viajeId, EstadoViaje nuevoEstado) {
        return viajeRepository.findById(viajeId).map(v -> {
            v.setEstado(nuevoEstado);
            return viajeRepository.save(v);
        });
    }
}