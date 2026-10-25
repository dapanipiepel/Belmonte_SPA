
package com.belmonte.usuarios_service.controller;

import com.belmonte.usuarios_service.model.Rol;
import com.belmonte.usuarios_service.service.RolService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/roles")
public class RolController {

    private final RolService rolService;

    public RolController(RolService rolService) {
        this.rolService = rolService;
    }

    @GetMapping
    public List<Rol> listarRoles() {
        return rolService.listarRoles();
    }

    @PostMapping
    public Rol crearRol(@RequestBody Rol rol) {
        return rolService.crearRol(rol);
    }
}
