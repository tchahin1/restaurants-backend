package com.example.restaurants.controllers;

import com.example.restaurants.dal.impl.RestaurantDao;
import com.example.restaurants.data.models.Restaurant;
import com.example.restaurants.repositories.RestaurantsRepository;
import com.example.restaurants.repositories.SearchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@CrossOrigin
@RestController
@RequestMapping(value = "/restaurants")
public class RestaurantsController {

    @Autowired
    private RestaurantDao restaurantDao;

    @Autowired
    private RestaurantsRepository restaurantsRepository;

    @Autowired
    private SearchRepository searchRepository;

    @GetMapping(value = "/all")
    public List<Restaurant> getRestaurants(){
        List<Restaurant> restorani = restaurantDao.getAll();
        List<Restaurant> restaurantFin = new ArrayList<Restaurant>();
        for(int i=0; i<6; i++) {
            Random generator = new Random();
            int randomIndex = generator.nextInt(restorani.size());
            restaurantFin.add(restorani.get(randomIndex));
            restorani.remove(randomIndex);
        }
        return restaurantFin;
    }

    @GetMapping(value = "/name")
    public ResponseEntity getRestaurant(@RequestParam("name") String name){
        //return restaurantDao.getRestaurantByName(name);
        Restaurant restaurants = restaurantsRepository.findRestaurantByName(name);
        return new ResponseEntity(restaurants, HttpStatus.OK);
    }

    @GetMapping("/search")
    public Page<List<Restaurant>> search(@RequestParam String query,
                                         @RequestParam Integer page,
                                         @RequestParam Integer size) {
        Page<List<Restaurant>> restaurantPage = searchRepository.findRestaurantsByNameContainsIgnoreCaseOrCity_NameContainsIgnoreCaseOrAddressContainsIgnoreCase(query, query,
                query, new PageRequest(page, size, new Sort(new Sort.Order(Sort.Direction.DESC, "name"))));
        return restaurantPage;
    }

    @GetMapping("/search/filterBy")
    public Page<List<Restaurant>> search(@RequestParam String query,
                                         @RequestParam Integer stars,
                                         @RequestParam Integer pricing,
                                         @RequestParam Integer page,
                                         @RequestParam Integer size) {

        Page<List<Restaurant>> restaurantPage;

        if(stars != 0 && pricing != 0) {
            restaurantPage = searchRepository.findRestaurantByNameContainsIgnoreCaseOrCity_NameContainsIgnoreCaseOrAddressContainsIgnoreCaseAndPricingIsAndStarsIs
                    (query, query, query, pricing, stars,
                            new PageRequest(page, size, new Sort(new Sort.Order(Sort.Direction.DESC, "name"))));
        }
        else if((stars !=0 && pricing == 0) || (stars == 0 && pricing != 0)){
            restaurantPage = searchRepository.findRestaurantByNameContainsIgnoreCaseOrCity_NameContainsIgnoreCaseOrAddressContainsIgnoreCaseAndPricingIsOrStarsIs
                    (query, query, query, pricing, stars,
                            new PageRequest(page, size, new Sort(new Sort.Order(Sort.Direction.DESC, "name"))));
        }
        else{
            restaurantPage = searchRepository.findRestaurantsByNameContainsIgnoreCaseOrCity_NameContainsIgnoreCaseOrAddressContainsIgnoreCase
                    (query, query, query,
                            new PageRequest(page, size, new Sort(new Sort.Order(Sort.Direction.DESC, "name"))));
        }

        return restaurantPage;
    }
}
