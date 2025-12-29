package core.api.DTO;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class SugerenciaDTO {
    private String descripcionSolicitud;
    private LocalDate fechaSugerencia;

    private String tituloSugerencia;
    private String descripcionSugerencia;
    private LocalDate fechaSucesoSugerencia;
    private LocalTime horaSucesoSugerencia;
    private String categoriaSugerencia;
    private List<String> etiquetasSugerencia;

    // datos del hecho
    private Integer idHecho;

    public SugerenciaDTO(){}

    public String getDescripcionSolicitud() {
        return descripcionSolicitud;
    }

    public void setDescripcionSolicitud(String descripcionSolicitud) {
        this.descripcionSolicitud = descripcionSolicitud;
    }

    public LocalDate getFechaSugerencia() {
        return fechaSugerencia;
    }

    public void setFechaSugerencia(LocalDate fechaSugerencia) {
        this.fechaSugerencia = fechaSugerencia;
    }

    public String getTituloSugerencia() {
        return tituloSugerencia;
    }

    public void setTituloSugerencia(String tituloSugerencia) {
        this.tituloSugerencia = tituloSugerencia;
    }

    public String getDescripcionSugerencia() {
        return descripcionSugerencia;
    }

    public void setDescripcionSugerencia(String descripcionSugerencia) {
        this.descripcionSugerencia = descripcionSugerencia;
    }

    public LocalDate getFechaSucesoSugerencia() {
        return fechaSucesoSugerencia;
    }

    public void setFechaSucesoSugerencia(LocalDate fechaSucesoSugerencia) {
        this.fechaSucesoSugerencia = fechaSucesoSugerencia;
    }

    public LocalTime getHoraSucesoSugerencia() {
        return horaSucesoSugerencia;
    }

    public void setHoraSucesoSugerencia(LocalTime horaSucesoSugerencia) {
        this.horaSucesoSugerencia = horaSucesoSugerencia;
    }

    public String getCategoriaSugerencia() {
        return categoriaSugerencia;
    }

    public void setCategoriaSugerencia(String categoriaSugerencia) {
        this.categoriaSugerencia = categoriaSugerencia;
    }

    public List<String> getEtiquetasSugerencia() {
        return etiquetasSugerencia;
    }

    public void setEtiquetasSugerencia(List<String> etiquetasSugerencia) {
        this.etiquetasSugerencia = etiquetasSugerencia;
    }

    public Integer getIdHecho() {
        return idHecho;
    }

    public void setIdHecho(Integer idHecho) {
        this.idHecho = idHecho;
    }
}
