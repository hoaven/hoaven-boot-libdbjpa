package com.wesd.persistence;

import lombok.extern.slf4j.Slf4j;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;
import java.util.Map;

@Slf4j
public abstract class AbstractBaseDao {

    protected abstract EntityManager getEntityManager();

    public <T> T insert(final T model) {
        this.getEntityManager().persist(model);
        if (model instanceof BaseReadOnlyModel) {
            log.info("created model {} id {}", model.getClass().getSimpleName(), ((BaseReadOnlyModel) model).getId());
        }
        return model;
    }

    public <T> T getById(Class<T> modelClass, final Object id) {
        if (id instanceof Integer) {
            return this.getEntityManager().find(modelClass, ((Integer) id).longValue());
        } else {
            return this.getEntityManager().find(modelClass, id);
        }
    }

    public <T> List<T> listQuery(final String hql, RowBounds rowBounds, Object... params) {
        Query query = this.getEntityManager().createQuery(hql);
        if (rowBounds != null) {
            query.setFirstResult(rowBounds.getOffset());
            query.setMaxResults(rowBounds.getLimit());
        }
        for (int i = 0; i < params.length; i++) {
            query.setParameter(i + 1, params[i]);
        }
        return query.getResultList();
    }

    public <T> List<T> listQuery(final String hql, RowBounds rowBounds, Map<String, Object> params) {
        Query query = this.getEntityManager().createQuery(hql);
        if (rowBounds != null) {
            query.setFirstResult(rowBounds.getOffset());
            query.setMaxResults(rowBounds.getLimit());
        }
        if (params != null) {
            for (Map.Entry<String, Object> entry : params.entrySet()) {
                query.setParameter(entry.getKey(), entry.getValue());
            }
        }
        return query.getResultList();
    }

    public <T> List<T> listQuery(final String hql, Map<String, Object> params) {
        Query query = this.getEntityManager().createQuery(hql);
        if (params != null) {
            for (Map.Entry<String, Object> entry : params.entrySet()) {
                query.setParameter(entry.getKey(), entry.getValue());
            }
        }
        return query.getResultList();
    }

    public <T> List<T> listQuery(final String hql, Object... params) {
        Query query = this.getEntityManager().createQuery(hql);
        for (int i = 0; i < params.length; i++) {
            query.setParameter(i + 1, params[i]);
        }
        return query.getResultList();
    }

    public <T> T singleBySQL(final String hql, Object... params) {
        Query query = this.getEntityManager().createQuery(hql);
        for (int i = 0; i < params.length; i++) {
            query.setParameter(i + 1, params[i]);
        }
        try {
            return (T) query.getSingleResult();
        } catch (javax.persistence.NoResultException e) {
            return null;
        }

    }

    public <T> T update(final T model) {
        this.getEntityManager().merge(model);
        if (model instanceof BaseModel) {
            log.info("updated model {} id {} version {}", model.getClass().getSimpleName(), ((BaseModel) model).getId(), ((BaseModel) model).getVersion());
        } else if (model instanceof BaseReadOnlyModel) {
            log.info("updated model {} id {}", model.getClass().getSimpleName(), ((BaseReadOnlyModel) model).getId());
        }
        return model;
    }

    public int update(String hql, Object... params) {
        Query query = this.getEntityManager().createQuery(hql);
        for (int i = 0; i < params.length; i++) {
            query.setParameter(i + 1, params[i]);
        }
        return query.executeUpdate();
    }

    public <T> void delete(final T model) {

        this.getEntityManager().remove(this.getEntityManager().merge(model));
        if (model instanceof BaseReadOnlyModel) {
            log.info("deleted model {} id {}", model.getClass().getSimpleName(), ((BaseModel) model).getId());
        }
    }

    public <T extends BaseModel> void remove(final T model) {
        this.getEntityManager().merge(model);
        if (model instanceof BaseModel) {
            log.info("removed model {} id {} version {}", model.getClass().getSimpleName(), ((BaseModel) model).getId(), ((BaseModel) model).getVersion());
        }
    }

}
