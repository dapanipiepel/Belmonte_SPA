
package com.belmonte.usuarios_service.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PruebaController {

    @GetMapping("/api/usuarios/prueba")
    public String probarBackend() {
        return "Backend de Belmonte funcionando correctamente";
    }
}
