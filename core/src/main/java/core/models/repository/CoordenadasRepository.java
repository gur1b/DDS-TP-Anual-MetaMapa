package core.models.repository;

import core.models.entities.hecho.Categoria;
import core.models.entities.hecho.Coordenadas;
import utils.DBUtils;

import javax.persistence.EntityManager;

public class CoordenadasRepository extends JpaRepositoryBase<Coordenadas, Integer> {

    private static volatile CoordenadasRepository instance;

    private CoordenadasRepository() {
        super(Coordenadas.class, DBUtils::getEntityManager, Coordenadas::getId);
    }

    public static CoordenadasRepository getInstance() {
        if (instance == null) {
            synchronized (CoordenadasRepository.class) {
                if (instance == null) instance = new CoordenadasRepository();
            }
        }
        return instance;
    }

    public Coordenadas getCoordenadas(Integer idCoordenadas){
        return findById(idCoordenadas);
    }

    public Boolean existe(Coordenadas coordenadas){
        return buscarPorCoordenadas(coordenadas) != null;
    }

    public Coordenadas buscarPorCoordenadas(Coordenadas coordenadas){
        if (coordenadas == null
                || coordenadas.getLatitud() == null
                || coordenadas.getLongitud() == null) return null;

        EntityManager em = DBUtils.getEntityManager();
        try {
            return (Coordenadas) em.createQuery(
                            "SELECT c FROM coordenadas c WHERE c.latitud = :latitud AND c.longitud = :longitud",
                            Coordenadas.class)
                    .setParameter("latitud", coordenadas.getLatitud())
                    .setParameter("longitud", coordenadas.getLongitud())
                    .getResultList()
                    .stream()
                    .findFirst()
                    .orElse(null);
        } finally {
            em.close();
        }
    }

}