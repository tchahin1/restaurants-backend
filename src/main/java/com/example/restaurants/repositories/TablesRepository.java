package com.example.restaurants.repositories;

import com.example.restaurants.data.models.Tables;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface TablesRepository extends CrudRepository<Tables, Long> {
    long countTablesByReservedIsFalseAndRestaurant_NameAndType(String name, Integer type);

    List<Tables> findDistinctByRestaurant_Name(String name);
}

