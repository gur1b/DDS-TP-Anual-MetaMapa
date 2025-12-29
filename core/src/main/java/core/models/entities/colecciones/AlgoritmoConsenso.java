package core.models.entities.colecciones;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import core.models.entities.fuentes.Fuente;
import core.models.entities.hecho.Hecho;
import core.models.agregador.HechoAIntegrarDTO;
import core.models.agregador.normalizador.ComparadorHechos;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = StrategyAbsoluta.class, name = "absoluto"),
        @JsonSubTypes.Type(value = StrategyMayoriaSimple.class, name = "mayoria simple"),
        @JsonSubTypes.Type(value = StrategyMultiplesMenciones.class, name = "multiples menciones"),
})

public abstract class AlgoritmoConsenso {

    public abstract List<Hecho> ejecutarAlgoritmo(List<Hecho> hechos, List<Fuente> fuentes);

    //múltiples menciones: si al menos dos fuentes contienen un mismo hecho y ninguna otra fuente contiene otro de igual título pero diferentes atributos, se lo considera consensuado;

    /*
    public List<Hecho> ejecutarAlgoritmo() {
        List<Hecho> hechosVisibles = new ArrayList<>();
        return hechosVisibles;
    }
    */

    public boolean esElMismoHecho(Hecho h1, Hecho h2) {
        return ComparadorHechos.getInstance().esElMismoHecho(convertirADTO(h1), convertirADTO(h2));
    }

    public boolean hechoDuplicado(Hecho h1, Hecho h2) {
        return ComparadorHechos.getInstance().hechoDuplicado(convertirADTO(h1), convertirADTO(h2));
    }

    public boolean esHechoSimilarDTO(HechoAIntegrarDTO a, HechoAIntegrarDTO b) {
        return (ComparadorHechos.getInstance().similitudDeHechos(a, b) >= 0.5 && ComparadorHechos.getInstance().similitudDeHechos(a, b) <0.8);
    }

    public boolean esHechoSimilar(Hecho h1, Hecho h2) {
        return  esHechoSimilarDTO(convertirADTO(h1), convertirADTO(h2));
    }

    public List<Hecho> obtenerHechosIguales(Hecho hecho, List<Hecho> hechos) {
        return hechos.stream()
                .filter(h -> esElMismoHecho(hecho, h))
                .collect(Collectors.toList());
    }

    public List<Hecho> obtenerHechosSimilares(Hecho hecho, List<Hecho> hechos) {
        return hechos.stream()
                .filter(h -> esHechoSimilar(hecho, h))
                .collect(Collectors.toList());
    }

    public double similitudHechos(Hecho h1, Hecho h2) {
        return ComparadorHechos.getInstance().similitudDeHechos(convertirADTO(h1), convertirADTO(h2));
    }

    private HechoAIntegrarDTO convertirADTO(Hecho hecho) {
        HechoAIntegrarDTO dto = new HechoAIntegrarDTO();
        dto.setTitulo(hecho.getTitulo());
        dto.setDescripcion(hecho.getDescripcion());
        dto.setCategoria(hecho.getCategoria().getNombre()); // o como lo tengas
        dto.setLatitud(hecho.getUbicacion().getLatitud().toString());
        dto.setLongitud(hecho.getUbicacion().getLongitud().toString());
        dto.setFechaSuceso(
                hecho.getFechaSuceso() != null ? hecho.getFechaSuceso().toString() : null
        );
        return dto;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        return true;
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    public String devolverTipoDeConsenso() {
        return null;
    }


}