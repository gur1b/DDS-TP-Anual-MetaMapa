package core.models.repository;

import core.models.entities.hecho.Contribuyente;
import utils.DBUtils;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import java.util.Optional;

public class ContribuyentesRepository extends JpaRepositoryBase<Contribuyente, Integer> {

    private static volatile ContribuyentesRepository instance;

    private ContribuyentesRepository() {
        super(Contribuyente.class, DBUtils::getEntityManager, Contribuyente::getId);
    }

    public static ContribuyentesRepository getInstance() {
        if (instance == null) {
            synchronized (ContribuyentesRepository.class) {
                if (instance == null) instance = new ContribuyentesRepository();
            }
        }
        return instance;
    }

    public Contribuyente getContribuyente(Integer idContribuyente){
        return findById(idContribuyente);
    }

    public Optional<Contribuyente> findByMail(String mail) {
        EntityManager em = DBUtils.getEntityManager();
        try {
            String jpql = """
            SELECT c FROM contribuyente c WHERE LOWER(c.mail) = :mail
            """;

            Contribuyente c = em.createQuery(jpql, Contribuyente.class)
                    .setParameter("mail", mail.trim().toLowerCase())
                    .getSingleResult();

            return Optional.of(c);

        } catch (NoResultException e) {
            return Optional.empty();
        } finally {
            em.close();
        }
    }

}
