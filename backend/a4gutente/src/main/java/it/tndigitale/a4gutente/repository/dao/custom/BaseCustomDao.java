package it.tndigitale.a4gutente.repository.dao.custom;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

public class BaseCustomDao {

    @PersistenceContext
    protected EntityManager entityManager;

    public BaseCustomDao setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
        return this;
    }

    public EntityManager getEntityManager() {
        return this.entityManager;
    }

    protected<T> List<T> executeJpaQuery(String jpqlQuery, Class<T> cl) {
        return entityManager.createQuery(jpqlQuery, cl).getResultList();
    }

}
