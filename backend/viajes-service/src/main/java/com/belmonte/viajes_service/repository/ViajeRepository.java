package com.belmonte.viajes_service.repository;

import com.belmonte.viajes_service.model.Viaje;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ViajeRepository extends JpaRepository<Viaje, Long> {
    List<Viaje> findByConductor(String conductor);
}