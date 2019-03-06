package com.example.restaurants.repositories;

import com.example.restaurants.data.models.Reservations;
import org.springframework.data.repository.CrudRepository;

public interface ReservationsRepository extends CrudRepository<Reservations, Long> {
}
