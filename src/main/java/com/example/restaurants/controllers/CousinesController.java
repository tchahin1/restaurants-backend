package com.example.restaurants.controllers;

import com.example.restaurants.data.models.Cousine;
import com.example.restaurants.repositories.CousinesRepository;
import com.example.restaurants.repositories.RestaurantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/cousines")
public class CousinesController {

    @Autowired
    private CousinesRepository cousinesRepository;

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Transactional
    @GetMapping(value = "/all")
    public ResponseEntity getAllCousines() {
        return new ResponseEntity(cousinesRepository.findDistinctCousines(), HttpStatus.OK);
    }

    @Transactional
    @GetMapping(value = "/delete")
    public ResponseEntity deleteCousine(@RequestParam String name) {
        cousinesRepository.deleteAllByName(name);
        return new ResponseEntity(HttpStatus.OK);
    }

    @GetMapping(value = "/get")
    public ResponseEntity getCousine(@RequestParam String name){
        return new ResponseEntity(cousinesRepository.findFirstByName(name),HttpStatus.OK);
    }

    @GetMapping(value = "/get/search")
    public ResponseEntity getCousines(@RequestParam String name){
        return new ResponseEntity(cousinesRepository.findByNameContainingIgnoreCase(name),HttpStatus.OK);
    }

    @GetMapping(value = "/save")
    public ResponseEntity saveCousine(@RequestParam String name){
        Cousine existingCousine = cousinesRepository.findFirstByName(name);
        if(existingCousine!=null) return new ResponseEntity(HttpStatus.BAD_REQUEST);
        Cousine cousine = new Cousine();
        cousine.setName(name);
        cousine.setRestaurant(null);
        cousinesRepository.save(cousine);
        return new ResponseEntity(HttpStatus.OK);
    }

    @GetMapping(value = "/edit")
    public ResponseEntity editCousine(@RequestParam String name, @RequestParam Long id){
        Cousine existingCousine = cousinesRepository.findCousineById(id);
        if(!(existingCousine.getName().equals(name))) {
            Cousine cousineTmp = cousinesRepository.findFirstByName(name);
            if(cousineTmp==null) {
                existingCousine.setName(name);
                cousinesRepository.save(existingCousine);
                return new ResponseEntity(HttpStatus.OK);
            }
            else return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity(HttpStatus.OK);
    }
}
