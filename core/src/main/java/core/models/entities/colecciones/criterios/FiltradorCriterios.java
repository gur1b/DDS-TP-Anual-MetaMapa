package core.models.entities.colecciones.criterios;

import core.models.entities.hecho.Hecho;
import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class FiltradorCriterios {
    private static volatile FiltradorCriterios instance;

    private FiltradorCriterios() {
        if (instance != null) {
            throw new RuntimeException("Usa getInstance() para obtener el Singleton");
        }
    }

    public static FiltradorCriterios getInstance() {
        if (instance == null) {
            synchronized (FiltradorCriterios.class) {
                if (instance == null) {
                    instance = new FiltradorCriterios();
                }
            }
        }
        return instance;
    }

    public Boolean cumpleCriterios(Hecho hecho, List<Criterio> criterios) {
        if(criterios == null || criterios.isEmpty()) {
            return true;
        }
        return criterios.stream().allMatch(criterio -> criterio.cumpleCriterio(hecho));
    }

    public List<Hecho> filtrarHechos(List<Hecho> hechos, List<Criterio> criterios) {
        if (hechos == null || hechos.isEmpty()) {
            return new ArrayList<>();
        }

        return hechos.stream()
                .filter(hecho -> this.cumpleCriterios(hecho, criterios))
                .collect(Collectors.toList());
    }
}