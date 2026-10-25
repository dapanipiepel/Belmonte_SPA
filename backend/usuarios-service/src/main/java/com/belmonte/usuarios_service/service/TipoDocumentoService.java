
package com.belmonte.usuarios_service.service;

import com.belmonte.usuarios_service.model.TipoDocumento;
import com.belmonte.usuarios_service.repository.TipoDocumentoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TipoDocumentoService {

    private final TipoDocumentoRepository tipoDocumentoRepository;

    public TipoDocumentoService(TipoDocumentoRepository tipoDocumentoRepository) {
        this.tipoDocumentoRepository = tipoDocumentoRepository;
    }

    public List<TipoDocumento> listarTiposDocumento() {
        return tipoDocumentoRepository.findAll();
    }

    public TipoDocumento crearTipoDocumento(TipoDocumento tipoDocumento) {
        return tipoDocumentoRepository.save(tipoDocumento);
    }
}
