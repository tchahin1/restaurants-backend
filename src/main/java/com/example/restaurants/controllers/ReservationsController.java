package com.example.restaurants.controllers;

import com.example.restaurants.data.models.Reservations;
import com.example.restaurants.data.models.Tables;
import com.example.restaurants.data.models.Users;
import com.example.restaurants.repositories.ReservationsRepository;
import com.example.restaurants.repositories.RestaurantsRepository;
import com.example.restaurants.repositories.TablesRepository;
import com.example.restaurants.repositories.UsersRepository;
import com.example.restaurants.services.ReservationsServices;
import org.hibernate.service.spi.InjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Array;
import java.sql.Time;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
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

    private  ReservationsServices reservationsServices;

    @CrossOrigin
    @GetMapping
    public ResponseEntity create(@RequestParam String date,
                                 @RequestParam String time,
                                 @RequestParam String username,
                                 @RequestParam String restaurant,
                                 @RequestParam Integer type){

       return reservationsServices.saveFunction(date, time, username, restaurant, type);
    }

    @CrossOrigin
    @GetMapping("/save/time")
    public ResponseEntity createByTime(@RequestParam String date,
                                       @RequestParam String time,
                                       @RequestParam String timeFrom,
                                       @RequestParam String username,
                                       @RequestParam String restaurant,
                                       @RequestParam Integer type){

        return reservationsServices.saveByTimeFunction(date, time, timeFrom, username, restaurant, type);
    }

    @CrossOrigin
    @GetMapping("/check/table")
    public ResponseEntity check(@RequestParam String date,
                                @RequestParam String time,
                                @RequestParam String username,
                                @RequestParam String restaurant,
                                @RequestParam Integer type){

        return reservationsServices.checkTable(date, time, username, restaurant, type);
    }

    @GetMapping("/check")
    public ResponseEntity check(@RequestParam String restaurant, @RequestParam String user){
        Reservations reservations = reservationsRepository.findByUser_EmailAndTable_Restaurant_Name(user, restaurant);
        if(reservations!=null) return new ResponseEntity(reservations, HttpStatus.BAD_REQUEST);
        return new ResponseEntity(reservations, HttpStatus.OK);
    }
}
