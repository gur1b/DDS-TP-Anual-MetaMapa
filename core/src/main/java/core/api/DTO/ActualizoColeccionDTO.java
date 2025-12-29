package core.api.DTO;

import core.api.DTO.criterio.CriterioDTO;

import java.util.List;

public class ActualizoColeccionDTO {
    public String titulo;
    public String descripcionColeccion;
    public List<CriterioDTO> criterioDePertenencia;
    public List<Integer> hechos; //CREO que no se usa para nada
    public List<Integer> fuentes;
    public String modoDeNavegacion;
    public String algoritmoConsenso;
    public ActualizoColeccionDTO() {}
}
