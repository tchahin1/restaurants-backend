package com.example.restaurants.controllers;

import com.example.restaurants.data.models.Reservations;
import com.example.restaurants.data.models.Tables;
import com.example.restaurants.repositories.ReservationsRepository;
import com.example.restaurants.repositories.TablesRepository;
import com.example.restaurants.repositories.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reservations")
public class ReservationsController {

    @Autowired
    ReservationsRepository reservationsRepository;

    @Autowired
    TablesRepository tablesRepository;

    @Autowired
    UsersRepository usersRepository;

    @Autowired
    ReservationsService reservationsService;

    @CrossOrigin
    @GetMapping
    public ResponseEntity create(@RequestParam String date,
                                 @RequestParam String time,
                                 @RequestParam String username,
                                 @RequestParam String restaurant,
                                 @RequestParam Integer type){

       return reservationsService.saveFunction(date, time, username, restaurant, type);
    }

    @CrossOrigin
    @GetMapping("/save/time")
    public ResponseEntity createByTime(@RequestParam String date,
                                       @RequestParam String time,
                                       @RequestParam String timeFrom,
                                       @RequestParam String username,
                                       @RequestParam String restaurant,
                                       @RequestParam Integer type){

        return reservationsService.saveByTimeFunction(date, time, timeFrom, username, restaurant, type);
    }

    @CrossOrigin
    @GetMapping("/check/table")
    public ResponseEntity check(@RequestParam String date,
                                @RequestParam String time,
                                @RequestParam String username,
                                @RequestParam String restaurant,
                                @RequestParam Integer type){

        return reservationsService.checkTable(date, time, username, restaurant, type);
    }

    @GetMapping("/check")
    public ResponseEntity check(@RequestParam String restaurant, @RequestParam String user){
        List<Reservations> reservations = reservationsRepository.findByUser_EmailAndTable_Restaurant_Name(user, restaurant);
        if(reservations!=null && reservations.size()!=0) {
            int i;
            for(i=0; i<reservations.size(); i++);
            i-=1;
            return new ResponseEntity(reservations.get(i), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity(reservations, HttpStatus.OK);
    }

    @CrossOrigin
    @GetMapping("/confirm")
    public ResponseEntity confirm(@RequestParam String username,
                                  @RequestParam String restaurant,
                                  @RequestParam Integer type){
        Reservations reservations = reservationsRepository.findByUser_EmailAndTable_Restaurant_NameAndTable_TypeAndTemporaryIsTrue(username, restaurant, type);

        reservations.setTemporary(false);
        reservationsRepository.save(reservations);
        return new ResponseEntity(HttpStatus.OK);
    }

    @CrossOrigin
    @GetMapping("/cancel")
    public ResponseEntity cancel(@RequestParam String username,
                                  @RequestParam String restaurant,
                                  @RequestParam Integer type){
        Reservations reservations = reservationsRepository.findByUser_EmailAndTable_Restaurant_NameAndTable_TypeAndTemporaryIsTrue(username, restaurant, type);
        Tables table = reservations.getTable();
        table.setReserved(false);
        tablesRepository.save(table);
        reservationsRepository.delete(reservations);
        return new ResponseEntity(HttpStatus.OK);
    }

    @CrossOrigin
    @GetMapping("/checkTemp")
    public ResponseEntity checkTemporaryReservation(@RequestParam String username){
        Reservations reservations = reservationsRepository.findByUser_EmailAndTemporaryIsTrue(username);
        if(reservations != null) return new ResponseEntity(reservations, HttpStatus.OK);
        else return new ResponseEntity(HttpStatus.FORBIDDEN);
    }
}
