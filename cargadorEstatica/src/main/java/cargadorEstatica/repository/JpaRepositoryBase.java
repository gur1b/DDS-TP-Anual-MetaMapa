package cargadorEstatica.repository;

import utils.DBUtils;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

public abstract class JpaRepositoryBase<T, ID> {

    private final Class<T> entityClass;
    private final Supplier<EntityManager> emSupplier;
    private final Function<T, ID> idGetter;

    protected JpaRepositoryBase(Class<T> entityClass,
                                Supplier<EntityManager> emSupplier,
                                Function<T, ID> idGetter) {
        this.entityClass = entityClass;
        this.emSupplier = emSupplier;
        this.idGetter = idGetter;
    }

    public T findById(ID id) {
        EntityManager em = emSupplier.get();
        try {
            return em.find(entityClass, id);
        } finally {
            em.close();
        }
    }

    public List<T> findAll() {
        EntityManager em = emSupplier.get();
        try {
            String ql = "SELECT e FROM " + entityClass.getSimpleName() + " e";
            TypedQuery<T> q = em.createQuery(ql, entityClass);
            return q.getResultList();
        } finally {
            em.close();
        }
    }


    public long count() {
        EntityManager em = emSupplier.get();
        try {
            String ql = "SELECT COUNT(e) FROM " + entityClass.getSimpleName() + " e";
            return em.createQuery(ql, Long.class).getSingleResult();
        } finally {
            em.close();
        }
    }


    public T save(T entity) {
        EntityManager em = emSupplier.get();
        try {
            DBUtils.comenzarTransaccion(em);
            ID id = idGetter.apply(entity);
            T managed;
            if (id == null) {
                em.persist(entity);
                managed = entity; // ya es managed
            } else {
                managed = em.merge(entity); // devuelve la instancia managed
            }
            DBUtils.commit(em);
            return managed;
        } catch (RuntimeException ex) {
            DBUtils.rollback(em);
            throw ex;
        } finally {
            em.close();
        }
    }


    public void delete(T entity) {
        EntityManager em = emSupplier.get();
        try {
            DBUtils.comenzarTransaccion(em);
            T managed = entity;
            if (!em.contains(entity)) {
                // reatachar para poder remover
                ID id = idGetter.apply(entity);
                if (id != null) {
                    managed = em.getReference(entityClass, id);
                } else {
                    managed = em.merge(entity);
                }
            }
            em.remove(managed);
            DBUtils.commit(em);
        } catch (RuntimeException ex) {
            DBUtils.rollback(em);
            throw ex;
        } finally {
            em.close();
        }
    }


    public void deleteById(ID id) {
        EntityManager em = emSupplier.get();
        try {
            DBUtils.comenzarTransaccion(em);
            T ref = em.find(entityClass, id);
            if (ref != null) em.remove(ref);
            DBUtils.commit(em);
        } catch (RuntimeException ex) {
            DBUtils.rollback(em);
            throw ex;
        } finally {
            em.close();
        }
    }

    public void update(T entity) {
        EntityManager em = emSupplier.get();
        try {
            DBUtils.comenzarTransaccion(em);
            T managed = em.merge(entity); // une el objeto detached con el contexto
            DBUtils.commit(em);
        } catch (RuntimeException ex) {
            DBUtils.rollback(em);
            throw ex;
        } finally {
            em.close();
        }
    }
}

