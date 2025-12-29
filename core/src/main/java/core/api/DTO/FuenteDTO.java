
package core.api.DTO;

import core.models.entities.fuentes.Fuente;
import core.models.entities.fuentes.StrategyTipoConexion;
import core.models.entities.fuentes.TipoFuente;

public class FuenteDTO {
    public Integer id;
    public String nombre;
    public String link;
    public String tipoFuente;
    public String strategyTipoConexion;

    public FuenteDTO(Integer id, String nombre, String tipoFuente, String link, String strategyTipoConexion) {
        this.id = id;
        this.nombre = nombre;
        this.tipoFuente = tipoFuente;
        this.link = link;
        this.strategyTipoConexion = strategyTipoConexion;
    }
    public FuenteDTO(String nombre, String tipoFuente, String link, String strategyTipoConexion) {
        this.nombre = nombre;
        this.tipoFuente = tipoFuente;
        this.link = link;
        this.strategyTipoConexion = strategyTipoConexion;
    }

    public FuenteDTO() {
    }

    public static FuenteDTO from(Fuente f) {
        return new FuenteDTO(f.getId(), f.getNombre(), f.getTipoFuente().toString(), f.getLink(), f.getStrategyTipoConexion().devolverTipoDeConexion());
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

    public String getTipoFuente() {
        return tipoFuente;
    }

    public void setTipoFuente(String tipoFuente) {
        this.tipoFuente = tipoFuente;
    }

    public String getStrategyTipoConexion() {
        return strategyTipoConexion;
    }

    public void setStrategyTipoConexion(String strategyTipoConexion) {
        this.strategyTipoConexion = strategyTipoConexion;
    }

    public Integer getId() {return id;}

    public void setId(Integer id) {this.id = id;}
}
