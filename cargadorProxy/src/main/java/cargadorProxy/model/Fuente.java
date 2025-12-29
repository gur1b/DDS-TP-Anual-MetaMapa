package cargadorProxy.model;


import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.time.Instant;
import java.util.List;


@Entity
@Table(name = "fuente")
public class Fuente {

    @Id
    private Integer id;
    @Column(name = "nombre")
    private String nombre;
    @Column(name = "link")
    private String link;
    @JsonIgnore
    @Convert(converter = StrategyTipoConexionConverter.class)
    @Column(name = "strategy_tipo_conexion")
    private StrategyTipoConexion strategyManeraDeObtenerHechos;
    @Column(name = "codigoFuente")
    private String codigoFuente;

    @JsonIgnore
    @Column(name = "ultimoProcesamiento")
    private Instant ultimoProcesamiento;

    public Fuente(){}

    public Instant getUltimoProcesamiento() { return ultimoProcesamiento; }
    public void setUltimoProcesamiento(Instant t) { this.ultimoProcesamiento = t; }

    public Fuente(Integer id, String nombre, String link, StrategyTipoConexion strategyManeraDeObtenerHechos, String codigoFuente) {
        this.id = id;
        this.nombre = nombre;
        this.link = link;
        this.strategyManeraDeObtenerHechos = strategyManeraDeObtenerHechos;
        this.codigoFuente = codigoFuente;
    }
    public Fuente( String nombre, String link, StrategyTipoConexion strategyManeraDeObtenerHechos, String codigoFuente) {
        this.nombre = nombre;
        this.link = link;
        this.strategyManeraDeObtenerHechos = strategyManeraDeObtenerHechos;
        this.codigoFuente = codigoFuente;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public StrategyTipoConexion getStrategyManeraDeObtenerHechos() {
        return strategyManeraDeObtenerHechos;
    }

    public void setStrategyManeraDeObtenerHechos(StrategyTipoConexion strategyManeraDeObtenerHechos) {
        this.strategyManeraDeObtenerHechos = strategyManeraDeObtenerHechos;
    }

    public String getCodigoFuente() {
        return codigoFuente;
    }

    public void setCodigoFuente(String codigoFuente) {
        this.codigoFuente = codigoFuente;
    }

    public List<HechoAIntegrarDTO> extraerHechos(){

        List<HechoAIntegrarDTO> hechos = strategyManeraDeObtenerHechos.extraerHechosRecientes(link, codigoFuente);
        hechos.forEach(h -> h.setTipoFuente("PROXY"));
        hechos.forEach(h -> h.setLinkFuente(this.getLink()));
        hechos.forEach(h -> h.setIdFuente(this.getId()));
        return hechos;
    }
}

