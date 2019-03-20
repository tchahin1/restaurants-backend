package com.example.restaurants.controllers;

import com.example.restaurants.data.models.Reservations;
import com.example.restaurants.data.models.Tables;
import com.example.restaurants.data.models.Users;
import com.example.restaurants.repositories.ReservationsRepository;
import com.example.restaurants.repositories.RestaurantsRepository;
import com.example.restaurants.repositories.TablesRepository;
import com.example.restaurants.repositories.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Time;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

@RestController
@RequestMapping("/reservations")
public class ReservationsController {

    @Autowired
    ReservationsRepository reservationsRepository;

    @Autowired
    TablesRepository tablesRepository;

    @Autowired
    UsersRepository usersRepository;

    @CrossOrigin
    @GetMapping
    public ResponseEntity create(@RequestParam String date,
                                 @RequestParam String time,
                                 @RequestParam String username,
                                 @RequestParam String restaurant,
                                 @RequestParam Integer type){

        if(time.charAt(0)!='0' && time.charAt(1)==':') time='0'+time;
        String datetime = date + " " + time;
        LocalDateTime localDateTimeFrom = LocalDateTime.parse(datetime, DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm"));

        LocalDateTime localDateTimeTo = localDateTimeFrom.plusHours(1);

        Reservations reservation = new Reservations();

        Tables table = tablesRepository.findByTypeAndReservedIsFalseAndRestaurant_Name(type, restaurant);
        Users user = usersRepository.findByEmail(username);

        reservation.setTable(table);
        reservation.setTimeFrom(localDateTimeFrom);
        reservation.setTimeTo(localDateTimeTo);
        reservation.setUser(user);

        reservationsRepository.save(reservation);
        table.setReserved(true);
        tablesRepository.save(table);

        return new ResponseEntity(HttpStatus.OK);
    }

    @CrossOrigin
    @GetMapping("/save/time")
    public ResponseEntity createByTime(@RequestParam String date,
                                       @RequestParam String time,
                                       @RequestParam String timeFrom,
                                       @RequestParam String username,
                                       @RequestParam String restaurant,
                                       @RequestParam Integer type){

        if(time.charAt(0)!='0' && time.charAt(1)==':') time='0'+time;
        String datetime = date + " " + time;
        LocalDateTime localDateTimeFrom = LocalDateTime.parse(datetime, DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm"));

        LocalDateTime localDateTimeTo = localDateTimeFrom.plusHours(1);

        if(timeFrom.charAt(0)!='0' && timeFrom.charAt(1)==':') timeFrom='0'+timeFrom;
        String datetimeWanted = date + " " +  timeFrom;
        LocalDateTime localDateTimeWanted = LocalDateTime.parse(datetimeWanted, DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm"));

        Reservations reservation = new Reservations();
        Users user = usersRepository.findByEmail(username);

        Reservations existingReservation = reservationsRepository.findByTable_TypeAndTable_Restaurant_NameAndTimeFromIsLessThanEqual
                (type, restaurant, localDateTimeWanted);

        reservation.setUser(user);
        reservation.setTable(existingReservation.getTable());
        reservation.setTimeFrom(localDateTimeFrom);
        reservation.setTimeTo(localDateTimeTo);

        reservationsRepository.save(reservation);

        return new ResponseEntity(HttpStatus.OK);
    }

    @CrossOrigin
    @GetMapping("/check/table")
    public ResponseEntity check(@RequestParam String date,
                                @RequestParam String time,
                                @RequestParam String username,
                                @RequestParam String restaurant,
                                @RequestParam Integer type){

        if(time.charAt(0)!='0' && time.charAt(1)==':') time='0'+time;
        String datetime = date + " " + time;
        LocalDateTime localDateTime = LocalDateTime.parse(datetime, DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm"));

        Reservations existingReservation = reservationsRepository.findByUser_EmailAndTable_TypeAndTable_Restaurant_Name
                (username, type, restaurant);

        if(existingReservation != null) {
            LocalDateTime timeFrom = existingReservation.getTimeFrom();
            LocalDateTime timeTo = existingReservation.getTimeTo();
            System.out.println(localDateTime);
            System.out.println(timeFrom);
            System.out.println(timeTo);
            System.out.println(localDateTime.isAfter(timeFrom));
            System.out.println(localDateTime.isBefore(timeTo));
            if(localDateTime.isAfter(timeFrom) && localDateTime.isBefore(timeTo)){
                return new ResponseEntity(HttpStatus.BAD_REQUEST);
            }
            else return new ResponseEntity(HttpStatus.BAD_GATEWAY);
        }

        Reservations takenReservation = reservationsRepository.findByTable_TypeAndTable_Restaurant_Name
                (type, restaurant);

        if(takenReservation != null && takenReservation.getUser().getUsername()!=username) {
            LocalDateTime timeFrom = takenReservation.getTimeFrom();
            LocalDateTime timeTo = takenReservation.getTimeTo();
            if(localDateTime.isAfter(timeFrom) && localDateTime.isBefore(timeTo)) {
                return new ResponseEntity(takenReservation, HttpStatus.CONFLICT);
            }
            else return new ResponseEntity(HttpStatus.BAD_GATEWAY);
        }

        return new ResponseEntity(HttpStatus.OK);
    }

    @GetMapping("/check")
    public ResponseEntity check(@RequestParam String restaurant, @RequestParam String user){
        Reservations reservations = reservationsRepository.findByUser_EmailAndTable_Restaurant_Name(user, restaurant);
        if(reservations!=null) return new ResponseEntity(reservations, HttpStatus.BAD_REQUEST);
        return new ResponseEntity(reservations, HttpStatus.OK);
    }
}
