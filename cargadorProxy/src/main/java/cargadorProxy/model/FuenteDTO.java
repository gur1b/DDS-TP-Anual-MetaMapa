package cargadorProxy.model;

public class FuenteDTO {
    private Integer id;
    private String nombre;
    private String link;
    private String tipoFuente;
    private String strategyTipoConexion;

    public FuenteDTO() {
    }

    public FuenteDTO(Integer id, String nombre, String link, String tipoFuente, String strategyTipoConexion) {
        this.id = id;
        this.nombre = nombre;
        this.link = link;
        this.tipoFuente = tipoFuente;
        this.strategyTipoConexion = strategyTipoConexion;
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

    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }

    public String getStrategyTipoConexion(){
        return strategyTipoConexion;
    }
    public void setStrategyTipoConexion(String strategyTipoConexion){
        this.strategyTipoConexion = strategyTipoConexion;
    }

    public static FuenteDTO from(Fuente fuente) {
        return new FuenteDTO(fuente.getId(), fuente.getNombre(), fuente.getLink(), "CSV", fuente.getStrategyManeraDeObtenerHechos().devolverTipoDeConexion());
    }
}
