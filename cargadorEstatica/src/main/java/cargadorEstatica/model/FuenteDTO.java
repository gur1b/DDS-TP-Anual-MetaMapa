package cargadorEstatica.model;

public class FuenteDTO {
    private Integer id;
    private String nombre;
    private String link;
    private String tipoFuente;

    public FuenteDTO(Integer id, String nombre, String link, String tipoFuente) {
        this.id = id;
        this.nombre = nombre;
        this.link = link;
        this.tipoFuente = tipoFuente;
    }

    public FuenteDTO() {
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

    public static FuenteDTO from(Fuente fuente) {
        return new FuenteDTO(fuente.getId(), fuente.getNombre(), fuente.getLink(), fuente.getStrategyManeraDeObtenerHechos().devolverTipoDeConexion());
    }
}
