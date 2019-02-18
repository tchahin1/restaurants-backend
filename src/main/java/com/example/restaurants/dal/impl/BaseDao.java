package com.example.restaurants.dal.impl;

import com.example.restaurants.dal.IBaseDao;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.io.Serializable;
import java.util.List;

@Component
public class BaseDao<T> implements IBaseDao<T> {

    private Class<T> cls;

    public Class<T> getCls() {
        return cls;
    }

    public void setCls(Class<T> cls) {
        this.cls = cls;
    }

    @PersistenceContext
    protected EntityManager entityManager;

    @Override
    public List<T> getAll() {

        return entityManager.createQuery(
            String.format("%s %s %s", "select e from", cls.getName(), "e"),
            cls
        ).getResultList();
    }

    @Override
    @Transactional
    public T save(T entity) {

        // fixme: add base entity class and add id check of entity
        // if entity id is null then save it, else throw IllegalArgumentException

        entityManager.persist(entity);
        return entity;
    }

    @Override
    public T getById(Serializable id) {

        T result = entityManager.find(cls, id);

        if (result == null) {

            throw new IllegalArgumentException(
                String.format("%s %s", "No entity found of type", cls.getSimpleName())
            );
        }

        return result;
    }

    @Override
    @Transactional
    public void delete(T entity) {
        entityManager.remove(entity);
    }

    @Override
    @Transactional
    public T update(T entity) {

        // fixme: add base entity class and add id check of entity
        // if entity id is not null then merge it, else throw IllegalArgumentException

        return entityManager.merge(entity);
    }
}
