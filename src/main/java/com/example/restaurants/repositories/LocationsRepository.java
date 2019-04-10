package com.example.restaurants.repositories;

import com.example.restaurants.data.models.Locations;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface LocationsRepository extends CrudRepository<Locations, Long> {
    Locations findByRestaurant_Id(Long num);
}
