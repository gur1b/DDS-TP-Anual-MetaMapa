package core.models.entities.colecciones;
import core.models.entities.fuentes.Fuente;
import core.models.entities.hecho.Hecho;
import core.models.entities.hecho.Estado;
import core.models.repository.HechosRepository;
import core.models.repository.FuentesRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


public class StrategyAbsoluta extends AlgoritmoConsenso {

    @Override
    public List<Hecho> ejecutarAlgoritmo(List<Hecho> hechos, List<Fuente> fuentes) {
        List<Hecho> hechosVisibles = new ArrayList<>();
        if (fuentes == null || fuentes.isEmpty()) return hechosVisibles;

        int cantidadFuentes = fuentes.size();

        // Filtrar solo hechos aceptados
        List<Hecho> hechosAceptados = hechos.stream()
                .filter(h -> h.getEstado() == Estado.ACEPTADO)
                .toList();

        for (Hecho hecho : hechosAceptados) {
            List<Hecho> grupo = obtenerHechosIguales(hecho, hechosAceptados);
            Set<Integer> idsFuentesEncontradas = grupo.stream()
                    .map(Hecho::getIdFuente)
                    .collect(Collectors.toSet());

            if (idsFuentesEncontradas.size() == cantidadFuentes) {
                boolean yaExiste = hechosVisibles.stream().anyMatch(hv -> esElMismoHecho(hv, hecho));
                if (!yaExiste) {
                    hechosVisibles.add(hecho);
                }
            }
        }
        return hechosVisibles;
    }

    @Override
    public String devolverTipoDeConsenso(){
        return "ABSOLUTO";
    }
}