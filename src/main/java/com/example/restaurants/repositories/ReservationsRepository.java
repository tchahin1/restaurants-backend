package com.example.restaurants.repositories;

import com.example.restaurants.data.models.Reservations;
import com.example.restaurants.data.models.Users;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface ReservationsRepository extends CrudRepository<Reservations, Long> {
    List<Reservations> findByUser_EmailAndTable_Restaurant_Name(String user, String restaurant);

    Reservations findByUser_EmailAndTable_TypeAndTable_Restaurant_Name
            (String username, Integer type, String restaurant);

    Reservations findFirstByTable_TypeAndTable_Restaurant_Name
            (Integer type, String restaurant);

    List<Reservations> findByTable_TypeAndTable_Restaurant_NameAndTimeFromIsLessThanEqual
            (Integer type, String restaurant, LocalDateTime time);

    Reservations findByTable_TypeAndTable_ReservedAndTimeFromAndTable_Restaurant_Name
            (Integer type, boolean reserved, LocalDateTime time, String restaurant);

    List<Reservations> findAllByTable_Id(Long num);

    List<Reservations> findAllByUser(Users users);

    Reservations findByUser_EmailAndTable_Restaurant_NameAndTable_TypeAndTemporaryIsTrue
            (String email, String restaurant, Integer type);
}
