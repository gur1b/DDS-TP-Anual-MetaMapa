package cargadorDinamica.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import javax.persistence.*;
import java.util.Collections;
import java.util.List;
@Entity
@Table(name = "hechos")
@JsonIgnoreProperties(ignoreUnknown = true)              // ignora campos extra en el JSON
@JsonInclude(JsonInclude.Include.NON_NULL)               // no serializa campos null en las respuestas
public class HechoAIntegrarDTO {

    @Id
    @Column(name= "hash")
    public String hash;

    @Column(name= "titulo")
    public String titulo;

    @Column(name= "descripcion")
    public String descripcion;

    @Column(name= "categoria")
    public String categoria;

    @Column(name= "latitud")
    public String latitud;

    @Column(name= "longitud")
    public String longitud;

    @Column(name= "fecha_suceso")
    public String fechaSuceso;

    @Column(name= "hora_suceso")
    public String horaSuceso;

    @Column(name= "fecha_carga")
    public String fechaCarga;

    @Column(name = "link_fuente")
    public String linkFuente;

    @ElementCollection
    @CollectionTable(
            name = "hecho_etiqueta",
            joinColumns = @JoinColumn(name = "hash", foreignKey = @ForeignKey(name = "hecho_etiqueta_hecho"))
    )
    @Column(name = "etiqueta", length = 100, nullable = false)
    public List<String> etiquetas;

    @Column (name = "contribuyente")
    public String contribuyente;

    @ElementCollection
    @CollectionTable(
            name = "hecho_multimedia",
            joinColumns = @JoinColumn(name = "hash",
                    foreignKey = @ForeignKey(name = "hecho_multimedia_hecho"))
    )
    @Column(name = "url", length = 500, nullable = false)
    public List<String> multimedia; //A CHEQUEAR !!!!

    @Column (name = "tipo_fuente")
    public String tipoFuente;

    @JsonProperty(access = Access.READ_ONLY)             // flag interno; no lo pidas en el request

    @Column (name = "fue_extraido")
    public Boolean fueExtraido;

    @Column (name = "idFuente")
    public Integer idFuente;

    public HechoAIntegrarDTO() {}

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public HechoAIntegrarDTO(
            @JsonProperty(value = "hash",         required = true) String hash,
            @JsonProperty(value = "titulo",       required = true) String titulo,
            @JsonProperty(value = "descripcion",  required = true) String descripcion,
            @JsonProperty(value = "categoria",    required = true) String categoria,
            @JsonProperty(value = "latitud",      required = true) String latitud,
            @JsonProperty(value = "longitud",     required = true) String longitud,
            @JsonProperty(value = "fechaSuceso",  required = true) String fechaSuceso
    ) {
        this.hash = hash;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.categoria = categoria;
        this.latitud = latitud;
        this.longitud = longitud;
        this.fechaSuceso = fechaSuceso;

        // opcionales: quedan null si no vienen
        this.horaSuceso = null;
        this.etiquetas = null;
        this.contribuyente = null;
        this.multimedia = null;
        this.tipoFuente = null;
        this.linkFuente = null;

        this.fueExtraido = Boolean.FALSE; // default interno
    }

    public HechoAIntegrarDTO(
            String hash, String titulo, String descripcion, String categoria, String latitud, String longitud, String fechaSuceso, String horaSuceso,
                             List<String> etiquetas, String contribuyente, List<String> multimedia)
    {
        this.hash = hash;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.categoria = categoria;
        this.latitud = latitud;
        this.longitud = longitud;
        this.fechaSuceso = fechaSuceso;
        this.horaSuceso = horaSuceso;
        this.etiquetas = etiquetas;
        this.contribuyente = contribuyente;
        this.multimedia = multimedia;
        this.tipoFuente = null;
    }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }
    public String getLatitud() { return latitud; }
    public void setLatitud(String latitud) { this.latitud = latitud; }
    public String getLongitud() { return longitud; }
    public void setLongitud(String longitud) { this.longitud = longitud; }
    public String getFechaSuceso() { return fechaSuceso; }
    public void setFechaSuceso(String fechaSuceso) { this.fechaSuceso = fechaSuceso; }
    public String getHash() { return hash; }
    public String getTipoFuente() {return tipoFuente;}
    public void setTipoFuente(String fuente) {this.tipoFuente = fuente;};
    public Integer getIdFuente() {return idFuente;}
    public void setIdFuente(Integer idFuente) {this.idFuente = idFuente;};
    public void setHash(String hash) { this.hash = hash; }
    public List<String> getEtiquetas() {
        return etiquetas;
    }

    public void setEtiquetas(List<String> etiquetas) {
        this.etiquetas = etiquetas;
    }

    public Boolean getFueExtraido() {
        return fueExtraido;
    }

    public void setFueExtraido(Boolean fueExtraido) {
        this.fueExtraido = fueExtraido;
    }

    public String getContribuyente() {
        return contribuyente;
    }

    public void setContribuyente(String contribuyente) {
        this.contribuyente = contribuyente;
    }

    public List<String> getMultimedia() {
        return multimedia;
    }

    public void setMultimedia(List<String> multimedia) {
        this.multimedia = multimedia;
    }

    public String getLinkFuente() {
        return linkFuente;
    }
    public void setLinkFuente(String linkFuente) {
        this.linkFuente = linkFuente;
    }

    public void setFechaCarga(String fechaCarga) {
        this.fechaCarga = fechaCarga;
    }
    public String getFechaCarga() {
        return fechaCarga;
    }

    public void setHoraSuceso(String horaSuceso) {
        this.horaSuceso = horaSuceso;
    }
    public String getHoraSuceso() {
        return horaSuceso;
    }


    @Override
    public String toString() {
        return "HechoAIntegrarDTO{" +
                "titulo='" + titulo + '\'' +
                ", descripcion='" + descripcion + '\'' +
                ", categoria='" + categoria + '\'' +
                ", latitud='" + latitud + '\'' +
                ", longitud='" + longitud + '\'' +
                ", fechaSuceso='" + fechaSuceso + '\'' +
                ", etiquetas=" + etiquetas +
                ", contribuyente='" + contribuyente + '\'' +
                ", multimedia=" + multimedia +
                '}';
    }

    public HechoAIntegrarDTO(String titulo, String descripcion, String categoria,
                             String latitud, String longitud, String fechaSuceso,
                             List<String> etiquetas, String contribuyente,
                             String multimedia, String tipoFuente,
                             Boolean fueExtraido, Integer idFuente) {
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.categoria = categoria;
        this.latitud = latitud;
        this.longitud = longitud;
        this.fechaSuceso = fechaSuceso;
        this.multimedia = Collections.singletonList(multimedia);
        this.etiquetas = etiquetas;
        this.contribuyente = contribuyente;
        this.tipoFuente = tipoFuente;
        this.fueExtraido = fueExtraido;
        this.idFuente = idFuente;
    }

}
