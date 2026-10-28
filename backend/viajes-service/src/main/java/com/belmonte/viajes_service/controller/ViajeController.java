package com.belmonte.viajes_service.controller;

import com.belmonte.viajes_service.model.EstadoViaje;
import com.belmonte.viajes_service.model.Viaje;
import com.belmonte.viajes_service.service.ViajeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/viajes")
@CrossOrigin(origins = "*")
public class ViajeController {

    private final ViajeService viajeService;

    public ViajeController(ViajeService viajeService) {
        this.viajeService = viajeService;
    }

    @GetMapping
    public List<Viaje> listarViajes() {
        return viajeService.obtenerTodos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Viaje> obtenerPorId(@PathVariable Long id) {
        return viajeService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Viaje> crearViaje(@RequestBody Viaje viaje) {
        Viaje nuevo = viajeService.crearViaje(viaje);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevo);
    }

    @PutMapping("/{id}/asignar")
    public ResponseEntity<?> asignarConductor(
            @PathVariable Long id,
            @RequestBody Map<String, Object> payload) {

        String conductor = (String) payload.get("conductor");
        Number vehiculoIdNum = (Number) payload.get("vehiculoId");

        if (conductor == null || vehiculoIdNum == null) {
            return ResponseEntity.badRequest().body("Se requiere conductor y vehiculoId");
        }

        return viajeService.asignarConductorYVehiculo(id, conductor, vehiculoIdNum.longValue())
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}/estado")
    public ResponseEntity<?> actualizarEstado(
            @PathVariable Long id,
            @RequestBody Map<String, String> payload) {

        String estadoStr = payload.get("estado");
        if (estadoStr == null) {
            return ResponseEntity.badRequest().body("Se requiere el campo 'estado'");
        }

        try {
            EstadoViaje estado = EstadoViaje.valueOf(estadoStr.toUpperCase());
            return viajeService.actualizarEstado(id, estado)
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Estado inválido: " + estadoStr);
        }
    }
}