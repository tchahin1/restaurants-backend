package com.example.restaurants.dal.impl;


import com.example.restaurants.data.models.Restaurant;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.TypedQuery;

@Repository
public class RestaurantDao extends BaseDao<Restaurant>{
    public RestaurantDao() {
        super.setCls(Restaurant.class);
    }

    @Transactional(readOnly = true)
    public Restaurant getRestaurantByName (String name) {

        TypedQuery<Restaurant> query =
                entityManager.createQuery(
                        "select r from Restaurant r where r.name = :name",
                        Restaurant.class
                );

        query.setParameter("name", name);
        return query.getSingleResult();
    }
}
