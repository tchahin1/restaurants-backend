package com.example.restaurants.repositories;

import com.example.restaurants.data.models.City;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CityRepository extends CrudRepository<City, Long> {
    City findByName(String name);
    List<City> findByNameContainingIgnoreCase(String name);
    City findCityById(Long num);
    List<City> findByCountryId(Long countryId);
}
