package core.models.repository;

import core.models.entities.colecciones.Coleccion;
import core.models.entities.fuentes.Fuente;
import core.models.entities.fuentes.TipoFuente;
import core.models.entities.hecho.Categoria;
import utils.DBUtils;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class FuentesRepository extends JpaRepositoryBase<Fuente, Integer> {
    private static volatile FuentesRepository instance;

    private FuentesRepository() {
        super(Fuente.class, DBUtils::getEntityManager, Fuente::getId);
    }

    public static FuentesRepository getInstance() {
        if (instance == null) {
            synchronized (FuentesRepository.class) {
                if (instance == null) {
                    instance = new FuentesRepository();
                }
            }
        }
        return instance;
    }


    //HACER
    public static List<Fuente> filtrarFuente(String tipoFuente) {
        TipoFuente tipoFuenteClase = TipoFuente.valueOf(tipoFuente.trim().toUpperCase());

        EntityManager em = DBUtils.getEntityManager();
        try {
            List<Fuente> resultados = em.createQuery(
                            "SELECT f FROM fuente f WHERE LOWER(TRIM(f.tipoFuente)) = LOWER(:tipoFuente)",
                            Fuente.class)
                    .setParameter(tipoFuente, tipoFuente.trim())
                    .getResultList();
            return resultados.isEmpty() ? null : resultados;

        } finally {
            em.close();
        }
    }

    public Fuente getFuente(int id)
    {return findById(id);}

    public List<Fuente> listarFuentesParaGraphQL() {
        EntityManager em = DBUtils.getEntityManager();
        try {
            List<Fuente> fuentes = em
                    .createQuery("select f from fuente f", Fuente.class)
                    .getResultList();

            // Inicializo lo que el schema GraphQL puede necesitar
            fuentes.forEach(f -> {
                f.getNombre();
                f.getLink();
                f.getTipoFuente();
            });

            return fuentes;
        } finally {
            em.close();
        }
    }

    public Fuente buscarFuenteParaGraphQL(Integer id) {
        EntityManager em = DBUtils.getEntityManager();
        try {
            Fuente f = em.find(Fuente.class, id);
            if (f == null) return null;

            f.getNombre();
            f.getLink();
            f.getTipoFuente();

            return f;
        } finally {
            em.close();
        }
    }


}
