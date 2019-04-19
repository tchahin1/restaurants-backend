package com.example.restaurants.controllers;

import com.example.restaurants.data.models.Locations;
import com.example.restaurants.data.models.Restaurant;
import com.example.restaurants.repositories.LocationsRepository;
import com.example.restaurants.repositories.RestaurantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/locations")
public class LocationsController {

    @Autowired
    private LocationsRepository locationsRepository;

    @Autowired
    private RestaurantRepository restaurantRepository;

    @GetMapping("/all")
    public ResponseEntity getLocations() {
        List<Locations> locations = (List<Locations>) locationsRepository.findAll();
        return new ResponseEntity(locations, HttpStatus.OK);
    }

    @PostMapping("/save")
    public ResponseEntity saveLocations(@RequestParam Double lat, @RequestParam Double lng, @RequestParam Long id){
        Locations locations = new Locations();
        locations.setLatitude(lat);
        locations.setLongitude(lng);
        Restaurant restaurant = restaurantRepository.findRestaurantById(id);
        locations.setRestaurant(restaurant);
        locationsRepository.save(locations);
        return new ResponseEntity(HttpStatus.OK);
    }

    @PostMapping("/edit")
    public ResponseEntity editLocations(@RequestParam Double lat, @RequestParam Double lng, @RequestParam Long id){
        Locations locations = locationsRepository.findByRestaurant_Id(id);
        if(locations!=null && (!(locations.getLatitude().equals(lat)) || !(locations.getLongitude().equals(lng)))){
            locations.setLatitude(lat);
            locations.setLongitude(lng);
            locationsRepository.save(locations);
        }
        return new ResponseEntity(HttpStatus.OK);
    }

}
