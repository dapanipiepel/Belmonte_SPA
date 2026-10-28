package com.belmonte.vehiculos_service.controller;

import com.belmonte.vehiculos_service.model.Vehiculo;
import com.belmonte.vehiculos_service.service.VehiculoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/vehiculos")
@CrossOrigin(origins = "*")
public class VehiculoController {

    private final VehiculoService vehiculoService;

    public VehiculoController(VehiculoService vehiculoService) {
        this.vehiculoService = vehiculoService;
    }

    @GetMapping
    public List<Vehiculo> listarVehiculos(@RequestParam(required = false, defaultValue = "true") boolean soloActivos) {
        if (soloActivos) {
            return vehiculoService.obtenerDisponibles();
        }
        return vehiculoService.obtenerTodos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Vehiculo> obtenerPorId(@PathVariable Long id) {
        return vehiculoService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Vehiculo> crearVehiculo(@RequestBody Vehiculo vehiculo) {
        Vehiculo nuevo = vehiculoService.guardar(vehiculo);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevo);
    }
}