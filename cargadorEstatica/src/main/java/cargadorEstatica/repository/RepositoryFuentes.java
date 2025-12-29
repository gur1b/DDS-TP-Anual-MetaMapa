package cargadorEstatica.repository;

import cargadorEstatica.model.Fuente;
import utils.DBUtils;

import java.util.ArrayList;
import java.util.List;

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