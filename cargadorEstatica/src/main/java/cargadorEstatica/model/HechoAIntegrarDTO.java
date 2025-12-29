package cargadorEstatica.model;

import java.util.List;

public class HechoAIntegrarDTO {
    private String titulo ;
    private String descripcion;
    private String categoria;
    private String latitud;
    private String longitud;
    private String fechaSuceso;
    private String horaSuceso;
    private List<String> etiquetas;
    private String contribuyente;
    private String multimedia;
    private String tipoFuente;
    private Boolean fueExtraido;
    private Integer idFuente;
    private String linkFuente;

    public HechoAIntegrarDTO(){}

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getLatitud() {
        return latitud;
    }

    public void setLatitud(String latitud) {
        this.latitud = latitud;
    }

    public String getLongitud() {
        return longitud;
    }

    public void setLongitud(String longitud) {
        this.longitud = longitud;
    }

    public String getFechaSuceso() {
        return fechaSuceso;
    }

    public void setFechaSuceso(String fechaSuceso) {
        this.fechaSuceso = fechaSuceso;
    }

    public List<String> getEtiquetas() {
        return etiquetas;
    }

    public void setEtiquetas(List<String> etiquetas) {
        this.etiquetas = etiquetas;
    }

    public String getContribuyente() {
        return contribuyente;
    }

    public void setContribuyente(String contribuyente) {
        this.contribuyente = contribuyente;
    }

    public String getMultimedia() {
        return multimedia;
    }

    public void setMultimedia(String multimedia) {
        this.multimedia = multimedia;
    }

    public String getTipoFuente() {
        return tipoFuente;
    }

    public void setTipoFuente(String tipoFuente) {
        this.tipoFuente = tipoFuente;
    }

    public Boolean getFueExtraido() {
        return fueExtraido;
    }

    public void setFueExtraido(Boolean fueExtraido) {
        this.fueExtraido = fueExtraido;
    }

    public Integer getIdFuente() {
        return idFuente;
    }

    public void setIdFuente(Integer idFuente) {
        this.idFuente = idFuente;
    }

    public String getLinkFuente() {
        return linkFuente;
    }

    public void setLinkFuente(String linkFuente) {
        this.linkFuente = linkFuente;
    }

    public String getHoraSuceso() {
        return horaSuceso;
    }
    public void setHoraSuceso(String horaSuceso) {
        this.horaSuceso = horaSuceso;
    }

    public HechoAIntegrarDTO(String titulo, String descripcion, String categoria, String latitud, String longitud, String fechaSuceso) {
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.categoria = categoria;
        this.latitud = latitud;
        this.longitud = longitud;
        this.fechaSuceso = fechaSuceso;
        this.linkFuente = null;
    }
}
