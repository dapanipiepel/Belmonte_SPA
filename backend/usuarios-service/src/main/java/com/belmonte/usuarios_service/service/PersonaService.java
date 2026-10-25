
package com.belmonte.usuarios_service.service;

import com.belmonte.usuarios_service.model.Pais;
import com.belmonte.usuarios_service.model.Persona;
import com.belmonte.usuarios_service.model.TipoDocumento;
import com.belmonte.usuarios_service.repository.PaisRepository;
import com.belmonte.usuarios_service.repository.PersonaRepository;
import com.belmonte.usuarios_service.repository.TipoDocumentoRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class PersonaService {

    private final PersonaRepository personaRepository;
    private final PaisRepository paisRepository;
    private final TipoDocumentoRepository tipoDocumentoRepository;

    public PersonaService(
            PersonaRepository personaRepository,
            PaisRepository paisRepository,
            TipoDocumentoRepository tipoDocumentoRepository) {
        this.personaRepository = personaRepository;
        this.paisRepository = paisRepository;
        this.tipoDocumentoRepository = tipoDocumentoRepository;
    }

    public List<Persona> listarPersonas() {
        return personaRepository.findAll();
    }

    public Persona crearPersona(Persona persona) {

        if (persona.getNombreCompleto() == null
                || persona.getNombreCompleto().isBlank()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "El nombre completo es obligatorio");
        }

        if (persona.getRutDni() == null
                || persona.getRutDni().isBlank()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "El RUT o DNI es obligatorio");
        }

        if (persona.getEmail() == null
                || persona.getEmail().isBlank()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "El correo electronico es obligatorio");
        }

        if (personaRepository.existsByEmail(persona.getEmail())) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Ya existe una persona con ese correo");
        }

        if (persona.getPaisDocumento() == null
                || persona.getPaisDocumento().getIdPais() == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Debe indicar el pais del documento");
        }

        if (persona.getTipoDocumento() == null
                || persona.getTipoDocumento().getIdTipoDocumento() == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Debe indicar el tipo de documento");
        }

        Pais pais = paisRepository.findById(
                persona.getPaisDocumento().getIdPais()
        ).orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "Pais no encontrado"));

        TipoDocumento tipoDocumento =
                tipoDocumentoRepository.findById(
                        persona.getTipoDocumento()
                                .getIdTipoDocumento()
                ).orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Tipo de documento no encontrado"));

        persona.setPaisDocumento(pais);
        persona.setTipoDocumento(tipoDocumento);

        return personaRepository.save(persona);
    }
}
