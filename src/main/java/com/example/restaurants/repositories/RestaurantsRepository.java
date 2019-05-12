package com.example.restaurants.repositories;

import com.example.restaurants.data.models.Restaurant;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface RestaurantsRepository extends CrudRepository<Restaurant, Long>, PagingAndSortingRepository<Restaurant, Long> {
    Restaurant findRestaurantByName(String name);

    void deleteByName(String name);

    List<Restaurant> findByNameContainingIgnoreCase(String name);

    Restaurant findRestaurantById(Long num);

    Restaurant findFirstByName(String name);

    List<Restaurant> findAllByCity_Id(Long num);

    @Query(value = "SELECT l.restaurant " +
            "FROM Locations l " +
            "WHERE 1 = 1 " +
            "ORDER BY ST_Distance(ST_MakePoint(?1,?2), ST_MakePoint(l.latitude, l.longitude))")
    List<Restaurant> sortRestaurants(Double lat, Double lon);
}
