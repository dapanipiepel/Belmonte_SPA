
package com.belmonte.usuarios_service.controller;

import com.belmonte.usuarios_service.model.TipoDocumento;
import com.belmonte.usuarios_service.service.TipoDocumentoService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/tipos-documento")
public class TipoDocumentoController {

    private final TipoDocumentoService tipoDocumentoService;

    public TipoDocumentoController(
            TipoDocumentoService tipoDocumentoService) {
        this.tipoDocumentoService = tipoDocumentoService;
    }

    @GetMapping
    public List<TipoDocumento> listarTiposDocumento() {
        return tipoDocumentoService.listarTiposDocumento();
    }

    @PostMapping
    public TipoDocumento crearTipoDocumento(
            @RequestBody TipoDocumento tipoDocumento) {
        return tipoDocumentoService.crearTipoDocumento(tipoDocumento);
    }
}
