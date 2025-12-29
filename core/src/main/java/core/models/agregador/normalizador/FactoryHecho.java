package core.models.agregador.normalizador;

import core.models.agregador.HechoAIntegrarDTO;
import core.models.entities.fuentes.TipoFuente;
import core.models.entities.hecho.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

//Esto va a pasar un hecho DTO a un hecho
public class FactoryHecho {
    private static volatile FactoryHecho instance;

    public FactoryHecho() {
        if (instance != null) {
            throw new RuntimeException("Usa getInstance() para obtener el Singleton");
        }
    }

    public static FactoryHecho getInstance() {
        if (instance == null) {
            synchronized (FactoryHecho.class) {
                if (instance == null) {
                    instance = new FactoryHecho();
                }
            }
        }
        return instance;
    }

    public Hecho convertirHecho(HechoAIntegrarDTO hecho, LocalDate fecha, Categoria categoria, Coordenadas ubicacion, List<Etiqueta> etiquetas, Contribuyente contribuyente, LocalTime horaSuceso){
        TipoFuente tipoFuente = TipoFuente.valueOf(hecho.getTipoFuente());

        // Si el DTO trae multimedia, la usamos; si no, dejamos null para que el flujo de agregación la complete con la de la base
        List<String> multimedia = hecho.getMultimedia();

        return new Hecho(
                null,
                ubicacion,
                categoria,
                null,
                LocalDate.now(),
                (multimedia != null && !multimedia.isEmpty()) ? multimedia : null,
                Estado.ACEPTADO,
                contribuyente,
                LocalDate.now(),
                fecha,
                horaSuceso,
                tipoFuente,
                etiquetas,
                hecho.getDescripcion(),
                hecho.getTitulo(),
                null,
                hecho.getHash(),
                hecho.getIdFuente(),
                hecho.getLinkFuente()
        );
    }

}
