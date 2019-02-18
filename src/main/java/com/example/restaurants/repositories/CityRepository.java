package com.example.restaurants.repositories;

import com.example.restaurants.data.models.City;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CityRepository extends CrudRepository<City, Long> {
    List<City> findByCountryId(Long id);
}
