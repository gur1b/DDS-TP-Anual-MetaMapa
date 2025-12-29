package core.models.entities.solicitud;

import core.models.entities.hecho.Hecho;

import javax.persistence.*;
import java.time.LocalDateTime;
@Entity(name = "solicitud_de_eliminacion")
public class SolicitudDeEliminacion {

    public SolicitudDeEliminacion(Integer id, Hecho hecho, String descripcion, Boolean aceptada, LocalDateTime fechaDeRevision) {
        this.id = id;
        this.hecho = hecho;
        this.descripcion = descripcion;
        this.aceptada = aceptada;
        this.fechaDeRevision = fechaDeRevision;
    }

    public SolicitudDeEliminacion(Integer id, Hecho hecho, String descripcion) {
        this.id = id;
        this.hecho = hecho;
        this.descripcion = descripcion;
        this.aceptada = null;
        this.fechaDeRevision = null;
    }

    public SolicitudDeEliminacion(Hecho hecho, String descripcion) {
        this.hecho = hecho;
        this.descripcion = descripcion;
        this.aceptada = null;
        this.fechaDeRevision = null;
    }


    public SolicitudDeEliminacion() {}

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }

    @ManyToOne
    private Hecho hecho;
    public Hecho getHecho() {
        return hecho;
    }
    public void setHecho(Hecho hecho) {
        this.hecho = hecho;
    }

    @Column(name = "descripcion")
    private String descripcion;
    public String getDescripcion() {
        return descripcion;
    }
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    @Column(name = "aceptada")
    private Boolean aceptada;
    public Boolean getAceptada() {
        return aceptada;
    }
    public void setAceptada(Boolean aceptada) {
        this.aceptada = aceptada;
    }

    @Column(name = "fechaDeRevision")
    private LocalDateTime fechaDeRevision;
    public LocalDateTime getFechaDeRevision() {
        return fechaDeRevision;
    }
    public void setFechaDeRevision(LocalDateTime fechaDeRevision) {
        this.fechaDeRevision = fechaDeRevision;
    }


    public void aceptarSolicitud() {
        // ya aceptada
        if (Boolean.TRUE.equals(this.aceptada)) {
            throw new IllegalStateException("La solicitud ya fue aceptada.");
        }
        // ya rechazada
        if (Boolean.FALSE.equals(this.aceptada)) {
            throw new IllegalStateException("La solicitud ya fue rechazada.");
        }
        // estaba pendiente (null), se acepta
        this.setAceptada(true);
        this.fechaDeRevision = LocalDateTime.now();
        hecho.desactivarse();
    }

    public void rechazarSolicitud() {
        if (Boolean.FALSE.equals(this.aceptada)) {
            throw new IllegalStateException("La solicitud ya fue rechazada.");
        }
        if (Boolean.TRUE.equals(this.aceptada)) {
            throw new IllegalStateException("La solicitud ya fue aceptada.");
        }
        hecho.activarse();
        this.setAceptada(false);
        this.setFechaDeRevision(LocalDateTime.now());
    }

    public String toString() {
        return "solicitud_de_eliminacion{" +
                "hecho=" + hecho +
                ", descripcion='" + descripcion + '\'' +
                ", aceptada=" + aceptada +
                ", fechaDeRevision=" + fechaDeRevision +
                '}';
    }


}
