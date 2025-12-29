package core.models.agregador.normalizador;

import core.models.entities.hecho.Etiqueta;
import core.models.repository.CoordenadasRepository;

import java.util.List;

public class NormalizadorEtiqueta {

    private static NormalizadorEtiqueta instance;

    public static NormalizadorEtiqueta getInstance() {
        if (instance == null) instance = new NormalizadorEtiqueta();

        return instance;
    }

    public List<Etiqueta> obtenerEtiquetas(List<String> etiquetasRaw){
        if (etiquetasRaw == null) return List.of();

        List<Etiqueta> etiquetas = etiquetasRaw.stream()
                .filter(e -> e != null && !e.isBlank())
                .map(Etiqueta::new)     // crea Etiqueta(nombre)
                .toList();
        
        return etiquetas;
    }
}
