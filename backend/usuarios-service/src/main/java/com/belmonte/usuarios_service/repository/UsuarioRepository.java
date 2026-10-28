
package com.belmonte.usuarios_service.repository;

import com.belmonte.usuarios_service.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    boolean existsByPersona_Email(String email);
    java.util.Optional<Usuario> findByPersona_Email(String email);

}
