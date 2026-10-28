package com.belmonte.vehiculos_service.model;

import jakarta.persistence.*;

@Entity
@Table(name = "vehiculos")
public class Vehiculo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String marca;

    @Column(nullable = false)
    private String modelo;

    @Column(nullable = false, unique = true)
    private String patente;

    @Column(nullable = false)
    private Integer capacidadPasajeros;

    @Column(nullable = false)
    private Boolean activo = true;

    public Vehiculo() {}

    public Vehiculo(String marca, String modelo, String patente, Integer capacidadPasajeros, Boolean activo) {
        this.marca = marca;
        this.modelo = modelo;
        this.patente = patente;
        this.capacidadPasajeros = capacidadPasajeros;
        this.activo = activo;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getMarca() { return marca; }
    public void setMarca(String marca) { this.marca = marca; }

    public String getModelo() { return modelo; }
    public void setModelo(String modelo) { this.modelo = modelo; }

    public String getPatente() { return patente; }
    public void setPatente(String patente) { this.patente = patente; }

    public Integer getCapacidadPasajeros() { return capacidadPasajeros; }
    public void setCapacidadPasajeros(Integer capacidadPasajeros) { this.capacidadPasajeros = capacidadPasajeros; }

    public Boolean getActivo() { return activo; }
    public void setActivo(Boolean activo) { this.activo = activo; }
}