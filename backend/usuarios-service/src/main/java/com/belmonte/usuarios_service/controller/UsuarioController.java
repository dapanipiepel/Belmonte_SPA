
package com.belmonte.usuarios_service.controller;

import com.belmonte.usuarios_service.model.Usuario;
import com.belmonte.usuarios_service.service.UsuarioService;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @PostMapping
    public Map<String, Object> registrarUsuario(
            @RequestBody Usuario usuario) {

        Usuario usuarioGuardado =
                usuarioService.registrarUsuario(usuario);

        return Map.of(
                "mensaje", "Usuario registrado correctamente",
                "idUsuario", usuarioGuardado.getIdUsuario(),
                "idPersona",
                usuarioGuardado.getPersona().getIdPersona(),
                "rol", usuarioGuardado.getRol().getNombreRol(),
                "activo", usuarioGuardado.getActivo()
        );
    }

    
@GetMapping
public List<Map<String, Object>> listarUsuarios() {

    return usuarioService.listarUsuarios()
            .stream()
            .map(usuario -> Map.<String, Object>of(
                    "idUsuario", usuario.getIdUsuario(),
                    "idPersona", usuario.getPersona().getIdPersona(),
                    "nombreCompleto", usuario.getPersona().getNombreCompleto(),
                    "email", usuario.getPersona().getEmail(),
                    "rol", usuario.getRol().getNombreRol(),
                    "activo", usuario.getActivo()
            ))
            .toList();
}


@PostMapping("/login")
public Map<String, Object> iniciarSesion(
        @RequestBody Map<String, String> datos) {

    String email = datos.get("email");
    String password = datos.get("password");

    if (email == null || email.isBlank()
            || password == null || password.isBlank()) {
        throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "Debe ingresar correo y contrasena"
        );
    }

    Usuario usuario = usuarioService.iniciarSesion(
            email, password
    );

    return Map.of(
            "mensaje", "Inicio de sesion correcto",
            "idUsuario", usuario.getIdUsuario(),
            "idPersona", usuario.getPersona().getIdPersona(),
            "nombreCompleto",
            usuario.getPersona().getNombreCompleto(),
            "email", usuario.getPersona().getEmail(),
            "rol", usuario.getRol().getNombreRol(),
            "activo", usuario.getActivo()
    );
}

}
