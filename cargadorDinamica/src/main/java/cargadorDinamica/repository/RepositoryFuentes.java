package cargadorDinamica.repository;

import cargadorDinamica.model.Fuente;
import utils.DBUtils;

public class RepositoryFuentes extends JpaRepositoryBase<Fuente, Integer> {
    private static volatile RepositoryFuentes instance;

    private RepositoryFuentes() {
        super(Fuente.class, DBUtils::getEntityManager, Fuente::getId);
    }

    public static RepositoryFuentes getInstance() {
        if (instance == null) {
            synchronized (RepositoryFuentes.class) {
                if (instance == null) {
                    instance = new RepositoryFuentes();
                }
            }
        }
        return instance;
    }
}
