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

public class StrategyMultiplesMenciones extends AlgoritmoConsenso {

    @Override
    public List<Hecho> ejecutarAlgoritmo(List<Hecho> hechos, List<Fuente> fuentes) {
        List<Hecho> hechosVisibles = new ArrayList<>();
        if (hechos == null || hechos.isEmpty()) return hechosVisibles;

        // Filtrar solo hechos aceptados
        List<Hecho> hechosAceptados = hechos.stream()
                .filter(h -> h.getEstado() == Estado.ACEPTADO)
                .toList();

        for (Hecho hecho : hechosAceptados) {
            // Agrupar hechos idénticos (por hash)
            List<Hecho> grupo = hechosAceptados.stream()
                    .filter(h -> h.getHash().equals(hecho.getHash()))
                    .toList();
            Set<Integer> idsFuentesEncontradas = grupo.stream()
                    .map(Hecho::getIdFuente)
                    .collect(Collectors.toSet());

            // Regla 1: Al menos 2 fuentes
            if (idsFuentesEncontradas.size() >= 2) {
                // Regla 2: Ninguna otra fuente tiene un hecho con el mismo título pero diferentes atributos
                boolean hayConflicto = hechosAceptados.stream()
                        .filter(h -> h.getTitulo().equals(hecho.getTitulo()) && !h.getHash().equals(hecho.getHash()))
                        .findAny()
                        .isPresent();
                if (!hayConflicto) {
                    boolean yaAgregado = hechosVisibles.stream()
                            .anyMatch(hv -> hv.getHash().equals(hecho.getHash()));
                    if (!yaAgregado) {
                        hechosVisibles.add(hecho);
                    }
                }
            }
        }
        return hechosVisibles;
    }

    @Override
    public String devolverTipoDeConsenso(){
        return "MULTIPLES_MENCIONES";
    }
}