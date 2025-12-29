package core.api.DTO;

import core.models.entities.hecho.Hecho;
import core.models.entities.solicitud.SolicitudDeEliminacion;

import java.time.LocalDate;
import java.time.LocalTime;

public class SolicitudConHechoDTO {

    private Integer id;
    private String descripcionSolicitud;
    private  Boolean aceptada;

    // datos del hecho
    private Integer idHecho;
    private String nombre;
    private String descripcionHecho;
    private String contribuyente;
    private LocalDate fechaCarga;
    private LocalDate fechaSuceso;
    private LocalTime horaSuceso;

    public SolicitudConHechoDTO(Integer id,
                                String descripcionSolicitud,
                                Integer idHecho,
                                String nombre,
                                String descripcionHecho,
                                String contribuyente,
                                LocalDate fechaCarga,
                                LocalDate fechaSuceso,
                                LocalTime horaSuceso,
                                Boolean estado) {
        this.id = id;
        this.descripcionSolicitud = descripcionSolicitud;
        this.idHecho = idHecho;
        this.nombre = nombre;
        this.descripcionHecho = descripcionHecho;
        this.contribuyente = contribuyente;
        this.fechaCarga = fechaCarga;
        this.fechaSuceso = fechaSuceso;
        this.horaSuceso = horaSuceso;
        this.aceptada = estado;
    }

    public static SolicitudConHechoDTO from(SolicitudDeEliminacion s) {
        Hecho h = s.getHecho();
        return new SolicitudConHechoDTO(
                s.getId(),
                s.getDescripcion(),
                h != null ? h.getId() : null,
                h != null ? h.getTitulo() : null,
                h != null ? h.getDescripcion() : null,
                (h != null && h.getContribuyente() != null)
                        ? h.getContribuyente().getNombreCompleto()
                        : null,
                h != null ? h.getFechaCarga() : null,
                h != null ? h.getFechaSuceso() : null,
                h != null ? h.getHoraSuceso() : null,
                s.getAceptada()
        );
    }

    // getters y setters
    public Integer getId() { return id; }
    public String getDescripcionSolicitud() { return descripcionSolicitud; }
    public String getNombre() { return nombre; }
    public String getDescripcionHecho() { return descripcionHecho; }
    public String getContribuyente() { return contribuyente; }
    public LocalDate getFechaCarga() { return fechaCarga; }
    public LocalDate getFechaSuceso() { return fechaSuceso; }
    public LocalTime getHoraSuceso() { return horaSuceso; }

    public Boolean getAceptada() {
        return aceptada;
    }

    public void setId(Integer id) { this.id = id; }
    public void setDescripcionSolicitud(String descripcionSolicitud) { this.descripcionSolicitud = descripcionSolicitud; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setDescripcionHecho(String descripcionHecho) { this.descripcionHecho = descripcionHecho; }
    public void setContribuyente(String contribuyente) { this.contribuyente = contribuyente; }
    public void setFechaCarga(LocalDate fechaCarga) { this.fechaCarga = fechaCarga; }
    public void setFechaSuceso(LocalDate fechaSuceso) { this.fechaSuceso = fechaSuceso; }
    public void setHoraSuceso(LocalTime horaSuceso) { this.horaSuceso = horaSuceso; }
    public void setAceptada(Boolean bool){this.aceptada = bool;}
}

