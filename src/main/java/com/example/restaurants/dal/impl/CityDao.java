package com.example.restaurants.dal.impl;

import com.example.restaurants.data.models.City;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.TypedQuery;
import java.util.List;

@Repository
public class CityDao extends BaseDao<City> {

    public CityDao() {
        super.setCls(City.class);
    }

    @Transactional(readOnly = true)
    public City
    getCity (String cityName, String countryName) {

        TypedQuery<City> query =
            entityManager.createQuery(
                "select ci " +
                   "from City ci " +
                   "inner join Country co on co.id = ci.country.id " +
                   "where ci.name = :cityName " +
                   "and co.name = :countryName",
                City.class
        );

        query.setParameter("cityName", cityName);
        query.setParameter("countryName", countryName);
        return query.getSingleResult();
    }

    @Transactional(readOnly = true)
    public List<City>
    getCityByCountry (String countryName, Long countryId) {

        TypedQuery<City> query =
                entityManager.createQuery(
                        "select ci " +
                                "from City ci " +
                                "inner join Country co on co.id = :countryId " +
                                "where co.name = :countryName",
                        City.class
                );

        query.setParameter("countryId", countryId);
        query.setParameter("countryName", countryName);
        return query.getResultList();
    }
}
