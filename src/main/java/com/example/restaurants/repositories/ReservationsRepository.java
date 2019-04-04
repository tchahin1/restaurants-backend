package com.example.restaurants.repositories;

import com.example.restaurants.data.models.Reservations;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDateTime;

public interface ReservationsRepository extends CrudRepository<Reservations, Long> {
    Reservations findByUser_EmailAndTable_Restaurant_Name(String user, String restaurant);

    Reservations findByUser_EmailAndTable_TypeAndTable_Restaurant_Name
            (String username, Integer type, String restaurant);

    Reservations findFirstByTable_TypeAndTable_Restaurant_Name
            (Integer type, String restaurant);

    Reservations findByTable_TypeAndTable_Restaurant_NameAndTimeFromIsLessThanEqual
            (Integer type, String restaurant, LocalDateTime time);

    Reservations findByTable_TypeAndTable_ReservedAndTimeFromAndTable_Restaurant_Name
            (Integer type, boolean reserved, LocalDateTime time, String restaurant);
}
