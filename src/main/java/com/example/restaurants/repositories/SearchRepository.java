package com.example.restaurants.repositories;

import com.example.restaurants.data.dtos.RestaurantDTO;
import com.example.restaurants.data.models.Restaurant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface SearchRepository extends PagingAndSortingRepository<Restaurant, Long> {
    Page<List<Restaurant>> findRestaurantsByNameContainsIgnoreCaseOrCity_NameContainsIgnoreCaseOrAddressContainsIgnoreCase(String name, String city_name, String address, Pageable var1);

    Page<List<Restaurant>> findRestaurantByNameContainsIgnoreCaseOrCity_NameContainsIgnoreCaseOrAddressContainsIgnoreCaseAndPricingOrStars
            (String name, String city_name, String address, Integer pricing, Integer rating, Pageable var1);

    Page<List<Restaurant>> findRestaurantByNameContainsIgnoreCaseAndPricingOrNameContainsIgnoreCaseAndStarsOrCity_NameContainsIgnoreCaseAndPricingOrCity_NameContainsIgnoreCaseAndStarsOrAddressContainsIgnoreCaseAndPricingOrAddressContainsIgnoreCaseAndStars
            (String name, Integer pricing, String name1, Integer stars, String city_name, Integer pricing1, String city_name1, Integer stars1, String address, Integer pricing2, String address1, Integer stars2, Pageable var1);

    Page<List<Restaurant>> findRestaurantByNameContainsIgnoreCaseOrCity_NameContainsIgnoreCaseOrAddressContainsIgnoreCaseAndPricingAndStars
            (String name, String city_name, String address, Integer pricing, Integer rating, Pageable var1);

    Page<List<Restaurant>> findRestaurantByNameContainsIgnoreCaseAndPricingAndStarsOrCity_NameContainsIgnoreCaseAndPricingAndStarsOrAddressContainsIgnoreCaseAndPricingAndStars
            (String name, Integer pricing, Integer stars, String city_name, Integer pricing1, Integer stars1, String address, Integer pricing2, Integer stars2, Pageable var1);

    /*@Query("select r from Restaurant r where r.name = ?1")
    Page<List<Restaurant>> findByEmailAddress(String emailAddress);*/
}
