package com.example.restaurants.repositories;

import com.example.restaurants.data.dtos.RestaurantDTO;
import com.example.restaurants.data.models.Restaurant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface SearchRepository extends PagingAndSortingRepository<Restaurant, Long> {
    Page<List<Restaurant>> findRestaurantsByNameOrCity_NameOrAddressContains(String name, String city_name, String address, Pageable var1);

    Page<List<Restaurant>> findRestaurantByNameOrCity_NameOrAddressContainsAndPricingIsLessThanEqualOrStarsIsLessThanEqual
            (String name, String city_name, String address, Integer pricing, Integer rating, Pageable var1);
}