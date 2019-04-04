package com.example.restaurants.repositories;

import com.example.restaurants.data.models.Tables;
import org.springframework.data.repository.CrudRepository;

import java.util.ArrayList;
import java.util.List;

public interface TablesRepository extends CrudRepository<Tables, Long> {
    long countTablesByReservedIsFalseAndRestaurant_NameAndType(String name, Integer type);

    List<Tables> findDistinctByRestaurant_Name(String name);

    Tables findFirstByTypeAndReservedIsFalseAndRestaurant_Name(Integer type, String name);

    Tables findByTypeIsBetween(Integer min, Integer max);

    ArrayList<Tables> findByReservedIsFalseAndRestaurant_Name(String name);
}

