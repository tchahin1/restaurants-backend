package com.example.restaurants.controllers;

import com.example.restaurants.data.models.City;
import com.example.restaurants.data.models.Cousine;
import com.example.restaurants.data.models.Restaurant;
import com.example.restaurants.data.models.Users;
import com.example.restaurants.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping(value = "/search")
public class SearchController {

    @Autowired
    private CousinesRepository cousinesRepository;

    @Autowired
    private RestaurantsRepository restaurantsRepository;

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private CitiesRepository citiesRepository;

    @GetMapping(value = "/get")
    public ResponseEntity getCities(@RequestParam String query){
        /*List<Cousine> cousines = cousinesRepository.findByNameContainingIgnoreCase(query);
        if(cousines==null || cousines.size()==0){
            List<City> cities = citiesRepository.findByNameContainingIgnoreCase(query);
            if(cities==null || cities.size()==0){
                List<Restaurant> restaurants = restaurantsRepository.findByNameContainingIgnoreCase(query);
                if(restaurants==null || restaurants.size()==0){
                    List<Users> users = usersRepository.findByNameContainingIgnoreCaseOrEmailContainingIgnoreCase(query, query);
                    if(users!=null) return new ResponseEntity(users, HttpStatus.OK);
                }
                else return new ResponseEntity(restaurants, HttpStatus.OK);
            }
            else return new ResponseEntity(cities, HttpStatus.OK);
        }
        else return new ResponseEntity(cousines, HttpStatus.OK);

        return new ResponseEntity(HttpStatus.BAD_REQUEST);*/


        ArrayList<Object> result = new ArrayList <Object>();
        List<Cousine> cousines = cousinesRepository.findByNameContainingIgnoreCase(query);
        List<City> cities = citiesRepository.findByNameContainingIgnoreCase(query);
        List<Restaurant> restaurants = restaurantsRepository.findByNameContainingIgnoreCase(query);
        List<Users> users = usersRepository.findByNameContainingIgnoreCaseOrEmailContainingIgnoreCase(query, query);
        for(int i=0; i<cousines.size(); i++){
            result.add(cousines.get(i));
        }
        for(int i=0; i<cities.size(); i++){
            result.add(cities.get(i));
        }
        for(int i=0; i<restaurants.size(); i++){
            result.add(restaurants.get(i));
        }
        for(int i=0; i<users.size(); i++){
            result.add(users.get(i));
        }
        return new  ResponseEntity(result, HttpStatus.OK);
    }
}
