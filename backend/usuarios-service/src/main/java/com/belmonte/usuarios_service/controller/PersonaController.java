
package com.belmonte.usuarios_service.controller;

import com.belmonte.usuarios_service.model.Persona;
import com.belmonte.usuarios_service.service.PersonaService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/personas")
public class PersonaController {

    private final PersonaService personaService;

    public PersonaController(PersonaService personaService) {
        this.personaService = personaService;
    }

    @GetMapping
    public List<Persona> listarPersonas() {
        return personaService.listarPersonas();
    }

    @PostMapping
    public Persona crearPersona(@RequestBody Persona persona) {
        return personaService.crearPersona(persona);
    }
}
