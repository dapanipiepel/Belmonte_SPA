package com.belmonte.viajes_service.model;

import jakarta.persistence.*;

@Entity
@Table(name = "viajes")
public class Viaje {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String pasajero;

    @Column(nullable = false)
    private Integer cantidadPasajeros;

    @Column(nullable = false)
    private String fecha;

    @Column(nullable = false)
    private String hora;

    @Column(nullable = false)
    private String origen;

    private Double latitudOrigen;
    private Double longitudOrigen;

    @Column(nullable = false)
    private String destino;

    private Double latitudDestino;
    private Double longitudDestino;

    private String conductor;
    private Long vehiculoId;
    private String numeroVuelo;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoViaje estado = EstadoViaje.SOLICITADO;

    public Viaje() {}

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getPasajero() { return pasajero; }
    public void setPasajero(String pasajero) { this.pasajero = pasajero; }

    public Integer getCantidadPasajeros() { return cantidadPasajeros; }
    public void setCantidadPasajeros(Integer cantidadPasajeros) { this.cantidadPasajeros = cantidadPasajeros; }

    public String getFecha() { return fecha; }
    public void setFecha(String fecha) { this.fecha = fecha; }

    public String getHora() { return hora; }
    public void setHora(String hora) { this.hora = hora; }

    public String getOrigen() { return origen; }
    public void setOrigen(String origen) { this.origen = origen; }

    public Double getLatitudOrigen() { return latitudOrigen; }
    public void setLatitudOrigen(Double latitudOrigen) { this.latitudOrigen = latitudOrigen; }

    public Double getLongitudOrigen() { return longitudOrigen; }
    public void setLongitudOrigen(Double longitudOrigen) { this.longitudOrigen = longitudOrigen; }

    public String getDestino() { return destino; }
    public void setDestino(String destino) { this.destino = destino; }

    public Double getLatitudDestino() { return latitudDestino; }
    public void setLatitudDestino(Double latitudDestino) { this.latitudDestino = latitudDestino; }

    public Double getLongitudDestino() { return longitudDestino; }
    public void setLongitudDestino(Double longitudDestino) { this.longitudDestino = longitudDestino; }

    public String getConductor() { return conductor; }
    public void setConductor(String conductor) { this.conductor = conductor; }

    public Long getVehiculoId() { return vehiculoId; }
    public void setVehiculoId(Long vehiculoId) { this.vehiculoId = vehiculoId; }

    public String getNumeroVuelo() { return numeroVuelo; }
    public void setNumeroVuelo(String numeroVuelo) { this.numeroVuelo = numeroVuelo; }

    public EstadoViaje getEstado() { return estado; }
    public void setEstado(EstadoViaje estado) { this.estado = estado; }
}