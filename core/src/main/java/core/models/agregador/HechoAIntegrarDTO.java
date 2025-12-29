package core.models.agregador;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import core.api.DTO.ArrayToStringDateTimeDeserializer;
import kotlin.jvm.Transient;

import java.util.Collections;
import java.util.List;
@JsonIgnoreProperties(ignoreUnknown = true) // ignora campos extra en el JSON
@JsonInclude(JsonInclude.Include.NON_NULL)               // no serializa campos null en las respuestas
public class HechoAIntegrarDTO {

    @JsonProperty(access = Access.READ_ONLY)             // no se espera en el JSON de entrada
    public String hash;

    public String titulo;
    public String descripcion;
    public String categoria;
    public String latitud;
    public String longitud;
    @JsonDeserialize(using = ArrayToStringDateTimeDeserializer.class)
    public String fechaSuceso;
    public String horaSuceso;
    public String linkFuente;

    public List<String> etiquetas;
    public String contribuyente;
    public List<String> multimedia; //A CHEQUEAR !!!!
    public String tipoFuente;

    @JsonProperty(access = Access.READ_ONLY)             // flag interno; no lo pidas en el request
    public Boolean fueExtraido;

    public Integer idFuente;

    public HechoAIntegrarDTO() {}

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public HechoAIntegrarDTO(
            @JsonProperty(value = "titulo",       required = true) String titulo,
            @JsonProperty(value = "descripcion",  required = true) String descripcion,
            @JsonProperty(value = "categoria",    required = true) String categoria,
            @JsonProperty(value = "latitud",      required = true) String latitud,
            @JsonProperty(value = "longitud",     required = true) String longitud,
            @JsonProperty(value = "fechaSuceso",  required = true) String fechaSuceso,
            @JsonProperty(value = "linkFuente",  required = true) String linkFuente
    ) {
        this.hash = HandlerRecientes.generarHash(titulo+descripcion+categoria+latitud+longitud);
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.categoria = categoria;
        this.latitud = latitud;
        this.longitud = longitud;
        this.fechaSuceso = fechaSuceso;
        this.linkFuente = linkFuente;

        // opcionales: quedan null si no vienen
        this.horaSuceso = null;
        this.etiquetas = null;
        this.contribuyente = null;
        this.multimedia = null;
        this.tipoFuente = null;

        this.fueExtraido = Boolean.FALSE; // default interno
    }

    public HechoAIntegrarDTO(String titulo, String descripcion, String categoria, String latitud, String longitud, String fechaSuceso, String horaSuceso,
                             List<String> etiquetas, String contribuyente, List<String> multimedia, String linkFuente)
    {
        this.hash = HandlerRecientes.generarHash(titulo+descripcion+categoria+latitud+longitud);
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
        this.linkFuente = linkFuente;
    }

    public HechoAIntegrarDTO(String titulo, String descripcion, String categoria, String latitud, String longitud, String fecha) {
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.categoria = categoria;
        this.latitud = latitud;
        this.longitud = longitud;
        this.fechaSuceso = fecha;
        this.etiquetas = null;
        this.contribuyente = null;
        this.multimedia = null;
        this.linkFuente = null;
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
    public String getLinkFuente() {return linkFuente;}
    public void setLinkFuente(String linkFuente){this.linkFuente = linkFuente;}
    public void setHash(String hash) {
        this.hash = hash;
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
    public List<String> getMultimedia() {
        return multimedia;
    }
    public void setMultimedia(List<String> multimedia) {
        this.multimedia = multimedia;
    }
    public Boolean getFueExtraido() {
        return fueExtraido;
    }
    public void setFueExtraido(Boolean fueExtraido) {
        this.fueExtraido = fueExtraido;
    }
    public String getHoraSuceso() {return horaSuceso;}
    public void setHoraSuceso(String horaSuceso) {this.horaSuceso = horaSuceso;}

    public Boolean tieneMismoTitulo(String tituloExterno){
        String tituloPropioLimpio = this.getTitulo().toLowerCase().replace(" ","");
        String tituloExternoLimpio = tituloExterno.toLowerCase().replace(" ","");
        //Los pone en minusculas y le elimina los espacios
        return tituloPropioLimpio.equals(tituloExternoLimpio);
    }

    @Override
    public  String toString() {
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
        this.linkFuente = null;
    }

}
