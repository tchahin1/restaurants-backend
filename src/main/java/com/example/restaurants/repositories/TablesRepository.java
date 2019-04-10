package com.example.restaurants.repositories;

import com.example.restaurants.data.models.Restaurant;
import com.example.restaurants.data.models.Tables;
import org.springframework.data.repository.CrudRepository;

import java.util.ArrayList;
import java.util.List;

public interface TablesRepository extends CrudRepository<Tables, Long> {
    long countTablesByReservedIsFalseAndRestaurant_NameAndType(String name, Integer type);

    List<Tables> findDistinctByRestaurant_Name(String name);

    Tables findFirstByTypeAndReservedIsFalseAndRestaurant_Name(Integer type, String name);

    List<Tables> findTablesByTypeIsBetween(Integer min, Integer max);

    ArrayList<Tables> findByReservedIsFalseAndRestaurant_Name(String name);

    ArrayList<Tables> findTablesByRestaurant_Id(Long num);

    ArrayList<Tables> findTablesByTypeAndRestaurant_Id(Integer type, Long num);

    Tables findFirstByTypeAndRestaurant_Id(Integer type, Long num);

    List<Tables> findAllByRestaurant(Restaurant restaurant);
}

