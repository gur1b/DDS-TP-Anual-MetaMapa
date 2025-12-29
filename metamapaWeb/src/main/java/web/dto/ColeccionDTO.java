package web.dto;

import java.util.List;

public record ColeccionDTO(
        Integer id,
        String titulo,
        String descripcionColeccion,
        List<CriterioDTO> criterioDePertenencia,
        Object hechos,
        Object hechosVisibles,
        String identificadorHandle,
        List<Integer> fuentes,
        Integer cantidadHechos,
        Integer cantidadHechosVisibles,
        String algoritmoConsenso,
        String modoDeNavegacion
) {}
