
package com.belmonte.usuarios_service.repository;

import com.belmonte.usuarios_service.model.Persona;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PersonaRepository extends JpaRepository<Persona, Long> {

    boolean existsByEmail(String email);

}
