package core.api.DTO;

import core.models.entities.hecho.Etiqueta;
import core.models.entities.hecho.Hecho;
import core.models.entities.hecho.SugerenciaDeCambio;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class SugerenciaConHechoDTO {

    private Integer id;
    private String descripcionSolicitud;
    private LocalDate fechaSugerencia;
    private Boolean aceptada;

    private String tituloSugerencia;
    private String descripcionSugerencia;
    private LocalDate fechaSucesoSugerencia;
    private LocalTime horaSucesoSugerencia;
    private String categoriaSugerencia;
    private List<String> etiquetasSugerencia;

    // datos del hecho
    private Integer idHecho;
    private String tituloHecho;
    private String descripcionHecho;
    private String contribuyenteHecho;
    private LocalDate fechaSucesoHecho;
    private LocalTime horaSucesoHecho;
    private String categoriaHecho;
    private List<String> etiquetasHecho;

    public static SugerenciaConHechoDTO from(SugerenciaDeCambio s) {
        Hecho h = s.getHecho();

        SugerenciaConHechoDTO dto = new SugerenciaConHechoDTO();

        // ===== Datos de la sugerencia =====
        dto.id = s.getId();
        dto.descripcionSolicitud = s.getDescripcionSugerencia();
        dto.fechaSugerencia = s.getFechaSugerencia();
        dto.aceptada = s.getAprobada();

        dto.tituloSugerencia = s.getTitulo();
        dto.descripcionSugerencia = s.getDescripcion();
        dto.fechaSucesoSugerencia = s.getFechaSugerencia();
        dto.horaSucesoSugerencia = s.getHoraSuceso();

        dto.categoriaSugerencia =
                s.getCategoria() != null ? s.getCategoria().getNombre() : null;

        dto.etiquetasSugerencia =
                s.getEtiquetas() != null
                        ? s.getEtiquetas().stream()
                        .map(Etiqueta::getNombre)
                        .toList()
                        : List.of();

        // ===== Datos del hecho original =====
        dto.idHecho = h != null ? h.getId() : null;
        dto.tituloHecho = h != null ? h.getTitulo() : null;
        dto.descripcionHecho = h != null ? h.getDescripcion() : null;

        dto.contribuyenteHecho =
                (h != null && h.getContribuyente() != null)
                        ? h.getContribuyente().getNombreCompleto()
                        : null;

        dto.fechaSucesoHecho = h != null ? h.getFechaSuceso() : null;
        dto.horaSucesoHecho  = h != null ? h.getHoraSuceso() : null;

        dto.categoriaHecho =
                (h != null && h.getCategoria() != null)
                        ? h.getCategoria().getNombre()
                        : null;

        dto.etiquetasHecho =
                (h != null && h.getEtiquetas() != null)
                        ? h.getEtiquetas().stream()
                        .map(Etiqueta::getNombre)
                        .toList()
                        : List.of();

        return dto;
    }

    public SugerenciaConHechoDTO(){}

    public SugerenciaConHechoDTO(Integer id, String descripcionSolicitud, LocalDate fechaSugerencia, Boolean estado, String tituloSugerencia, String descripcionSugerencia, LocalDate fechaSucesoSugerencia, LocalTime horaSucesoSugerencia, String categoriaSugerencia, List<String> etiquetasSugerencia, Integer idHecho, String tituloHecho, String descripcionHecho, String contribuyenteHecho, LocalDate fechaSucesoHecho, LocalTime horaSucesoHecho, String categoriaHecho, List<String> etiquetasHecho) {
        this.id = id;
        this.descripcionSolicitud = descripcionSolicitud;
        this.fechaSugerencia = fechaSugerencia;
        this.aceptada = estado;
        this.tituloSugerencia = tituloSugerencia;
        this.descripcionSugerencia = descripcionSugerencia;
        this.fechaSucesoSugerencia = fechaSucesoSugerencia;
        this.horaSucesoSugerencia = horaSucesoSugerencia;
        this.categoriaSugerencia = categoriaSugerencia;
        this.etiquetasSugerencia = etiquetasSugerencia;
        this.idHecho = idHecho;
        this.tituloHecho = tituloHecho;
        this.descripcionHecho = descripcionHecho;
        this.contribuyenteHecho = contribuyenteHecho;
        this.fechaSucesoHecho = fechaSucesoHecho;
        this.horaSucesoHecho = horaSucesoHecho;
        this.categoriaHecho = categoriaHecho;
        this.etiquetasHecho = etiquetasHecho;
    }


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

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

    public Boolean getAceptada() {
        return aceptada;
    }

    public void setAceptada(Boolean aceptada) {
        this.aceptada = aceptada;
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

    public String getTituloHecho() {
        return tituloHecho;
    }

    public void setTituloHecho(String tituloHecho) {
        this.tituloHecho = tituloHecho;
    }

    public String getDescripcionHecho() {
        return descripcionHecho;
    }

    public void setDescripcionHecho(String descripcionHecho) {
        this.descripcionHecho = descripcionHecho;
    }

    public String getContribuyenteHecho() {
        return contribuyenteHecho;
    }

    public void setContribuyenteHecho(String contribuyenteHecho) {
        this.contribuyenteHecho = contribuyenteHecho;
    }

    public LocalDate getFechaSucesoHecho() {
        return fechaSucesoHecho;
    }

    public void setFechaSucesoHecho(LocalDate fechaSucesoHecho) {
        this.fechaSucesoHecho = fechaSucesoHecho;
    }

    public LocalTime getHoraSucesoHecho() {
        return horaSucesoHecho;
    }

    public void setHoraSucesoHecho(LocalTime horaSucesoHecho) {
        this.horaSucesoHecho = horaSucesoHecho;
    }

    public String getCategoriaHecho() {
        return categoriaHecho;
    }

    public void setCategoriaHecho(String categoriaHecho) {
        this.categoriaHecho = categoriaHecho;
    }

    public List<String> getEtiquetasHecho() {
        return etiquetasHecho;
    }

    public void setEtiquetasHecho(List<String> etiquetasHecho) {
        this.etiquetasHecho = etiquetasHecho;
    }
}
