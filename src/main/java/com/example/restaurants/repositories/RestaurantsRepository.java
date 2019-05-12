package com.example.restaurants.repositories;

import com.example.restaurants.data.models.Restaurant;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.lang.Nullable;

import java.util.List;

public interface RestaurantsRepository extends CrudRepository<Restaurant, Long>, PagingAndSortingRepository<Restaurant, Long> {
    Restaurant findRestaurantByName(String name);

    void deleteByName(String name);

    List<Restaurant> findByNameContainingIgnoreCase(String name);

    Restaurant findRestaurantById(Long num);

    Restaurant findFirstByName(String name);

    List<Restaurant> findAllByCity_Id(Long num);

    @Query(value = "SELECT r " +
            "FROM Locations l " +
            "INNER JOIN Restaurant r ON r.id = l.restaurant.id " +
            "INNER JOIN City c ON c.id = r.city.id " +
            "WHERE (" +
            "LOWER(c.name) LIKE CONCAT('%', LOWER(?3), '%') " +
            "OR LOWER(r.name) LIKE CONCAT('%', LOWER(?3), '%') " +
            "OR LOWER(r.address) LIKE CONCAT('%', LOWER(?3), '%')" +
            ") " +
            "AND (?4 = -1 OR ?4 = r.pricing) " +
            "AND (?5 = -1 OR ?5 = r.stars) " +
            "ORDER BY ST_Distance(ST_MakePoint(?1,?2), ST_MakePoint(l.latitude, l.longitude))")
    List<Restaurant> sortRestaurants(Double lat, Double lon, String query, Integer pricing, Integer stars);
}
