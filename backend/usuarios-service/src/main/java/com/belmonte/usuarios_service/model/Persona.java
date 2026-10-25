
package com.belmonte.usuarios_service.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

import java.time.LocalDateTime;

@Entity
@Table(name = "personas")
public class Persona {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_persona")
    private Long idPersona;

    @ManyToOne(optional = false)
    @JoinColumn(name = "pais_documento_id", nullable = false)
    private Pais paisDocumento;

    @ManyToOne(optional = false)
    @JoinColumn(name = "tipo_documento_id", nullable = false)
    private TipoDocumento tipoDocumento;

    @Column(name = "rut_dni", nullable = false, length = 30)
    private String rutDni;

    @Column(name = "nombre_completo", nullable = false, length = 100)
    private String nombreCompleto;

    @Column(name = "telefono", length = 20)
    private String telefono;

    @Column(name = "email", nullable = false, unique = true, length = 120)
    private String email;

    @Column(name = "fecha_creacion", nullable = false, updatable = false)
    private LocalDateTime fechaCreacion;

    public Persona() {
    }

    @PrePersist
    public void asignarFechaCreacion() {
        if (fechaCreacion == null) {
            fechaCreacion = LocalDateTime.now();
        }
    }

    public Long getIdPersona() {
        return idPersona;
    }

    public void setIdPersona(Long idPersona) {
        this.idPersona = idPersona;
    }

    public Pais getPaisDocumento() {
        return paisDocumento;
    }

    public void setPaisDocumento(Pais paisDocumento) {
        this.paisDocumento = paisDocumento;
    }

    public TipoDocumento getTipoDocumento() {
        return tipoDocumento;
    }

    public void setTipoDocumento(TipoDocumento tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }

    public String getRutDni() {
        return rutDni;
    }

    public void setRutDni(String rutDni) {
        this.rutDni = rutDni;
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }
}
