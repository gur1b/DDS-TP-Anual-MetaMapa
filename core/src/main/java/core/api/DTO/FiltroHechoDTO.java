package core.api.DTO;

import java.time.LocalDate;

public class FiltroHechoDTO {

    private String titulo;
    private String descripcion;
    private String etiqueta;
    private String categoria;
    private String provincia;
    private Boolean soloMultimedia;
    private LocalDate fechaDesdeSuceso;
    private LocalDate fechaHastaSuceso;
    private LocalDate fechaDesdeCarga;
    private LocalDate fechaHastaCarga;


    public FiltroHechoDTO() {
    }

    public FiltroHechoDTO(String titulo, String descripcion, String etiqueta, String categoria, String provincia,
                          Boolean soloMultimedia, LocalDate fechaDesdeSuceso, LocalDate fechaHastaSuceso,
                          LocalDate fechaDesdeCarga, LocalDate fechaHastaCarga) {
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.etiqueta = etiqueta;
        this.categoria = categoria;
        this.provincia = provincia;
        this.soloMultimedia = soloMultimedia;
        this.fechaDesdeSuceso = fechaDesdeSuceso;
        this.fechaHastaSuceso = fechaHastaSuceso;
        this.fechaDesdeCarga = fechaDesdeCarga;
        this.fechaHastaCarga = fechaHastaCarga;
    }

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

    public String getEtiqueta() {
        return etiqueta;
    }

    public void setEtiqueta(String etiqueta) {
        this.etiqueta = etiqueta;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getProvincia() {
        return provincia;
    }

    public void setProvincia(String provincia) {
        this.provincia = provincia;
    }

    public Boolean getSoloMultimedia() {
        return soloMultimedia;
    }

    public void setSoloMultimedia(Boolean soloMultimedia) {
        this.soloMultimedia = soloMultimedia;
    }

    public LocalDate getFechaDesdeSuceso() {
        return fechaDesdeSuceso;
    }

    public void setFechaDesdeSuceso(LocalDate fechaDesdeSuceso) {
        this.fechaDesdeSuceso = fechaDesdeSuceso;
    }

    public LocalDate getFechaHastaSuceso() {
        return fechaHastaSuceso;
    }

    public void setFechaHastaSuceso(LocalDate fechaHastaSuceso) {
        this.fechaHastaSuceso = fechaHastaSuceso;
    }

    public LocalDate getFechaDesdeCarga() {
        return fechaDesdeCarga;
    }

    public void setFechaDesdeCarga(LocalDate fechaDesdeCarga) {
        this.fechaDesdeCarga = fechaDesdeCarga;
    }

    public LocalDate getFechaHastaCarga() {
        return fechaHastaCarga;
    }

    public void setFechaHastaCarga(LocalDate fechaHastaCarga) {
        this.fechaHastaCarga = fechaHastaCarga;
    }
}