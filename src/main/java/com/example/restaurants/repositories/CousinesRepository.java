package com.example.restaurants.repositories;

import com.example.restaurants.data.models.Cousine;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CousinesRepository extends CrudRepository<Cousine, Long> {
    void deleteAllByName(String name);

    @Query("SELECT c FROM Cousine c WHERE c.id IN (SELECT MIN(c2.id) FROM Cousine c2 GROUP BY c2.name)")
    List<Cousine> findDistinctCousines();

    Cousine findFirstByName(String name);

    List<Cousine> findByNameContainingIgnoreCase(String name);

    Cousine findCousineById(Long num);

    Cousine findFirstByNameAndRestaurantIsNull(String name);

    Cousine findFirstByRestaurant_Id(Long num);
}
