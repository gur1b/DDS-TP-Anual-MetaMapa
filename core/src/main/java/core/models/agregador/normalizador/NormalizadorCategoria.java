package core.models.agregador.normalizador;

import core.models.entities.hecho.Categoria;
import core.models.agregador.HechoAIntegrarDTO;
import core.models.repository.CategoriaRepository;

import java.util.List;
import java.util.Objects;

public class NormalizadorCategoria {

    private static NormalizadorCategoria instance;
    private CategoriaRepository categoriaRepository = CategoriaRepository.getInstance();
    private ComparadorHechos comparadorHechos = ComparadorHechos.getInstance();

    public static NormalizadorCategoria getInstance() {
        if (instance == null) instance = new NormalizadorCategoria();

        return instance;
    }


    public Categoria obtenerCategoria(String categoria) {
        Categoria categoriaNueva = new Categoria(categoria);
        return categoriaNueva;
    }

    public void estandarizarCategoriasDuplicadas(List<HechoAIntegrarDTO> hechos) {
        Objects.requireNonNull(hechos, "lista nula");
        for (int i = 0; i < hechos.size(); i++) {
            HechoAIntegrarDTO hi = hechos.get(i);
            for (int j = i + 1; j < hechos.size(); ) {
                HechoAIntegrarDTO hj = hechos.get(j);
                if (tienenDistintaCategoria(hi,hj) && comparadorHechos.esElMismoHecho(hi, hj)) {
                    hj.setCategoria(hi.getCategoria());
                } else {
                    j++;
                }
            }
        }
    }

    public Boolean tienenDistintaCategoria(HechoAIntegrarDTO hecho1, HechoAIntegrarDTO hecho2) {
        return !hecho1.getCategoria().equals(hecho2.getCategoria());
    }

}
