package core.models.entities.fuentes;

import com.fasterxml.jackson.annotation.JsonIgnore;
import core.models.agregador.HechoAIntegrarDTO;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;


@Entity(name = "fuente")
public class Fuente {

    public Fuente(String nombre, String link, TipoFuente tipoFuente, StrategyTipoConexion strategyTipoConexion) {
        this.nombre = nombre;
        this.link = link;
        this.tipoFuente = tipoFuente;
        this.strategyTipoConexion = strategyTipoConexion;
    }

    public Fuente(Integer id, String nombre, String link, TipoFuente tipoFuente, StrategyTipoConexion strategyTipoConexion) {
        this.nombre = nombre;
        this.link = link;
        this.tipoFuente = tipoFuente;
        this.strategyTipoConexion = strategyTipoConexion;
        this.id = id;
    }

    public  Fuente(){}

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_fuente")
    private Integer id;
    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }

    @Column(name = "nombre")
    private String nombre;
    public String getNombre() {
        return nombre;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @Column(name = "link")
    private String link;
    public String getLink() {
        return link;
    }
    public void setLink(String link) {
        this.link = link;
    }

    @Column(name = "tipo_fuente")
    private TipoFuente tipoFuente;
    public TipoFuente getTipoFuente() {return tipoFuente;
    }
    public void setTipoFuente(TipoFuente tipoFuente) {
        this.tipoFuente = tipoFuente;
    }

    @JsonIgnore
    @Convert(converter = StrategyTipoConexionConverter.class)
    @Column(name = "strategy_tipo_conexion")
    private StrategyTipoConexion strategyTipoConexion;
    public StrategyTipoConexion getStrategyTipoConexion() {
        return strategyTipoConexion;
    }
    public void setStrategyTipoConexion(StrategyTipoConexion strategyTipoConexion) {
        this.strategyTipoConexion = strategyTipoConexion;
    }

    public List<HechoAIntegrarDTO> extraerHechos(){
        return strategyTipoConexion.extraerHecho(link);
    };
}

