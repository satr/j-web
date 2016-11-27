package io.github.satr.jweb.components.repositories;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class HibernateRepositoryBase<T> implements Repository<T> {
    private final EntityManagerFactory entityManagerFactory;

    public HibernateRepositoryBase() {
        entityManagerFactory = Persistence.createEntityManagerFactory("persistenceUnit");
    }

    @Override
    public List<T> getList() throws SQLException {
        return getQueryable(getSqlForList(), null);
    }

    protected abstract String getSqlForList();

    @Override
    public T get(int id) throws SQLException {
        EntityManager entityManager = null;
        try {
            entityManager = entityManagerFactory.createEntityManager();
            return (T)entityManager.find(getEntityClass(), id);
        }
        finally {
            closeEntityManager(entityManager);
        }
    }

    /*
    * Example:
    * getQueryable("from EntityName where email = :email and ID > :id",
    *                                                (query) -> {
    *                                                   query.setParameter("email", email);
    *                                                   query.setParameter("id", 10);
    *                                                });
    * */
    protected List<T> getQueryable(String sql, QueryParamsDelegate setParamsDelegate) throws SQLException {
        EntityManager entityManager = null;
        try {
            entityManager = entityManagerFactory.createEntityManager();
            Query query = entityManager.createQuery(sql);
            if(setParamsDelegate != null)
                setParamsDelegate.setParams(query);
            return query.getResultList();
        }
        catch(Exception ex) {
            ex.printStackTrace();
            return new ArrayList<T>();
        }
        finally {
            closeEntityManager(entityManager);
        }
    }

    protected T getQueryableEnity(String sql, QueryParamsDelegate setParamsDelegate) throws SQLException {
        List<T> list = getQueryable(sql, setParamsDelegate);
        return list.size() == 0 ? null : list.get(0);
    }

    protected abstract Class getEntityClass();

    @Override
    public void save(T entity) throws SQLException {
        if(entity == null)
            return;
        save(Arrays.asList(entity));
    }

    @Override
    public void save(List<T> entities) {
        if(entities == null || entities.size() == 0)
            return;
        EntityManager entityManager = null;
        try {
            entityManager = entityManagerFactory.createEntityManager();
            entityManager.getTransaction().begin();
            for (T entity: entities) {
                if (entityManager.contains(entity)) {
                    entityManager.persist(entity);
                    entityManager.flush();
                } else {
                    entityManager.merge(entity);
                }
            }
            entityManager.getTransaction().commit();
            entityManager.getTransaction().begin();
            entityManager.flush();
            entityManager.getTransaction().commit();
        }
        catch(Exception ex) {
            rollbackTransaction(entityManager);
            throw ex;
        }
        finally {
            closeEntityManager(entityManager);
        }
    }

    @Override
    public void remove(T entity) {
        if(entity == null)
            return;
        remove(Arrays.asList(entity));
    }

    @Override
    public void remove(List<T> entities) {
        if(entities == null || entities.size() == 0)
            return;
        EntityManager entityManager = null;
        try {
            entityManager = entityManagerFactory.createEntityManager();
            entityManager.getTransaction().begin();
            for(T entity: entities)
                entityManager.remove(entityManager.merge(entity));
            entityManager.getTransaction().commit();
        }
        catch(Exception ex) {
            rollbackTransaction(entityManager);
            throw ex;
        }
        finally {
            closeEntityManager(entityManager);
        }

    }

    public void closeEntityManager(EntityManager entityManager) {
        if(entityManager != null && entityManager.isOpen())
            entityManager.close();
    }

    public void rollbackTransaction(EntityManager entityManager) {
        if (entityManager != null && entityManager.isOpen() && entityManager.getTransaction().isActive())
            entityManager.getTransaction().rollback();
    }

    protected interface QueryParamsDelegate {
        void setParams(Query query);
    }
}
