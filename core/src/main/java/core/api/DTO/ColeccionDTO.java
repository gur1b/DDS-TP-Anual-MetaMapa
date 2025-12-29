package core.api.DTO;

import core.api.DTO.criterio.CriterioDTO;

import java.util.List;

public class ColeccionDTO {

    public ColeccionDTO(){}

    public ColeccionDTO(Integer id, String titulo, String descripcionColeccion, List<CriterioDTO> listaCriterio, List<Integer> fuentes, List<Integer> hechos, String identificadorHandle) {
        this.id = id;
        this.titulo = titulo;
        this.descripcionColeccion = descripcionColeccion;
        this.criterioDePertenencia = listaCriterio;
        this.fuentes = fuentes;
        this.hechos = hechos;
        this.identificadorHandle = identificadorHandle;
    }

    public ColeccionDTO(Integer id, String titulo, String descripcionColeccion, List<CriterioDTO> listaCriterio, List<Integer> fuentes, List<Integer> hechos, String identificadorHandle, Integer cantidadHechos) {
        this.id = id;
        this.titulo = titulo;
        this.descripcionColeccion = descripcionColeccion;
        this.criterioDePertenencia = listaCriterio;
        this.fuentes = fuentes;
        this.hechos = hechos;
        this.identificadorHandle = identificadorHandle;
        this.cantidadHechos = cantidadHechos;
    }

    private Integer id;
    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }

    private String titulo;
    public String getTitulo() {
        return titulo;
    }
    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    private String descripcionColeccion;
    public String getDescripcionColeccion() {
        return descripcionColeccion;
    }
    public void setDescripcionColeccion(String descripcionColeccion) {
        this.descripcionColeccion = descripcionColeccion;
    }

    private List<CriterioDTO> criterioDePertenencia;
    public List<CriterioDTO> getCriterioDePertenencia() {
        return criterioDePertenencia;
    }
    public void setCriterioDePertenencia(List<CriterioDTO> criterioDePertenencia) {
        this.criterioDePertenencia = criterioDePertenencia;
    }

    private List<Integer> hechos;
    public List<Integer> getHechos() {
        return hechos;
    }
    public void setHechos(List<Integer> hechos) {
        this.hechos = hechos;
    }

    private String identificadorHandle;
    public String getIdentificadorHandle() {
        return identificadorHandle;
    }
    public void setIdentificadorHandle(String identificadorHandle) {
        this.identificadorHandle = identificadorHandle;
    }

    private List<Integer> fuentes;
    public List<Integer> getFuentes() {
        return fuentes;
    }
    public void setFuentes(List<Integer> fuentes) {
        this.fuentes = fuentes;
    }

    public Integer getCantidadHechos() {
        return cantidadHechos;
    }

    public void setCantidadHechos(Integer cantidadHechos) {
        this.cantidadHechos = cantidadHechos;
    }

    private Integer cantidadHechos;

    private String modoDeNavegacion;
    public String getModoDeNavegacion() {
        return modoDeNavegacion;
    }
    public void setModoDeNavegacion(String modoDeNavegacion) {
        this.modoDeNavegacion = modoDeNavegacion;
    }

    private String algoritmoConsenso;
    public String getAlgoritmoConsenso() {
        return algoritmoConsenso;
    }
    public void setAlgoritmoConsenso(String algoritmoConsenso) {
        this.algoritmoConsenso = algoritmoConsenso;
    }

    private Integer cantidadHechosVisibles;

    public Integer getCantidadHechosVisibles() {
        return cantidadHechosVisibles;
    }

    public void setCantidadHechosVisibles(Integer cantidadHechosVisibles) {
        this.cantidadHechosVisibles = cantidadHechosVisibles;
    }


}
