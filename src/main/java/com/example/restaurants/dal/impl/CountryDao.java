package com.example.restaurants.dal.impl;

import com.example.restaurants.data.models.Country;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.TypedQuery;

@Component
public class CountryDao extends BaseDao<Country> {

    public CountryDao() {
        super.setCls(Country.class);
    }

    @Transactional(readOnly = true)
    public Country
    getCityByName (String name) {

        TypedQuery<Country> query =
            entityManager.createQuery(
                "select c from Country c where c.name = :name",
                Country.class
        );

        query.setParameter("name", name);
        return query.getSingleResult();
    }
}
