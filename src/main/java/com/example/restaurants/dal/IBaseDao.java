package com.example.restaurants.dal;

import java.io.Serializable;
import java.util.List;

public interface IBaseDao<T> {

    void setCls(Class<T> cls);

    Class<T> getCls();

    List<T> getAll();

    T getById(Serializable id) throws IllegalArgumentException;

    T save(T entity);

    void delete(T entity);

    T update(T entity);
}
