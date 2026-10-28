
package com.belmonte.usuarios_service.controller;

import com.belmonte.usuarios_service.model.Pais;
import com.belmonte.usuarios_service.service.PaisService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/paises")
public class PaisController {

    private final PaisService paisService;

    public PaisController(PaisService paisService) {
        this.paisService = paisService;
    }

    @GetMapping
    public List<Pais> listarPaises() {
        return paisService.listarPaises();
    }

    @PostMapping
    public Pais crearPais(@RequestBody Pais pais) {
        return paisService.crearPais(pais);
    }
}
