package com.example.restaurants.repositories;

import com.example.restaurants.data.models.City;
import com.example.restaurants.data.models.Country;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CountryRepository extends CrudRepository<Country, Long> {
    Country findCountryById(Long id);
    Country findByName(String name);

    void deleteByName(String name);

    List<Country> findByNameContainingIgnoreCase(String name);

    Country findCountryByNameContainingIgnoreCaseOrNameIsLikeIgnoreCase(String name, String name1);
}
