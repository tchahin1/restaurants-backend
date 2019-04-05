package com.example.restaurants.controllers;

import com.example.restaurants.dal.impl.RestaurantDao;
import com.example.restaurants.data.models.City;
import com.example.restaurants.data.models.Cousine;
import com.example.restaurants.data.models.Pictures;
import com.example.restaurants.data.models.Restaurant;
import com.example.restaurants.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.io.Console;
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

    @Autowired
    private CitiesRepository citiesRepository;

    @Autowired
    private CousinesRepository cousinesRepository;

    @Autowired
    private PicturesRepository picturesRepository;

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

    @GetMapping(value = "/find/all")
    public ResponseEntity getAllRestaurants(){
        return new ResponseEntity(restaurantsRepository.findAll(), HttpStatus.OK);
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
            restaurantPage = searchRepository.findRestaurantByNameContainsIgnoreCaseAndPricingAndStarsOrCity_NameContainsIgnoreCaseAndPricingAndStarsOrAddressContainsIgnoreCaseAndPricingAndStars
                    (query, pricing, stars, query, pricing, stars, query, pricing, stars,
                            new PageRequest(page, size, new Sort(new Sort.Order(Sort.Direction.DESC, "name"))));
        }
        else if((stars !=0 && pricing == 0) || (stars == 0 && pricing != 0)){
            restaurantPage = searchRepository.findRestaurantByNameContainsIgnoreCaseAndPricingOrNameContainsIgnoreCaseAndStarsOrCity_NameContainsIgnoreCaseAndPricingOrCity_NameContainsIgnoreCaseAndStarsOrAddressContainsIgnoreCaseAndPricingOrAddressContainsIgnoreCaseAndStars
                    (query, pricing, query, stars, query, pricing, query, stars, query, pricing, query, stars,
                            new PageRequest(page, size, new Sort(new Sort.Order(Sort.Direction.DESC, "name"))));
        }
        else{
            restaurantPage = searchRepository.findRestaurantsByNameContainsIgnoreCaseOrCity_NameContainsIgnoreCaseOrAddressContainsIgnoreCase
                    (query, query, query,
                            new PageRequest(page, size, new Sort(new Sort.Order(Sort.Direction.DESC, "name"))));
        }

        return restaurantPage;
    }

    @GetMapping("/count")
    @ResponseStatus(HttpStatus.OK)
    public long restaurantsCount(){
        return restaurantsRepository.count();
    }

    @Transactional
    @GetMapping(value = "/delete")
    public ResponseEntity deleteRestaurant(@RequestParam String name) {
        restaurantsRepository.deleteByName(name);
        return new ResponseEntity(HttpStatus.OK);
    }

    @GetMapping(value = "/get/search")
    public ResponseEntity getRestaurants(@RequestParam String name){
        return new ResponseEntity(restaurantsRepository.findByNameContainingIgnoreCase(name),HttpStatus.OK);
    }

    @PostMapping("/saveLogo")
    public ResponseEntity saveLogo(@RequestParam String pictureUrl, @RequestParam Long id){
        Restaurant restaurant = restaurantsRepository.findRestaurantById(id);
        Pictures pictures = new Pictures();
        pictures.setLogoUrl(pictureUrl);
        pictures.setRestaurant(restaurant);
        picturesRepository.save(pictures);
        pictures = picturesRepository.findPicturesByRestaurant_Id(id);
        return new ResponseEntity(pictures, HttpStatus.OK);
    }

    @PostMapping("/saveCover")
    public ResponseEntity saveCover(@RequestParam String pictureUrl, @RequestParam Long id){
        Pictures pictures = picturesRepository.findPicturesById(id);
        pictures.setCoverUrl(pictureUrl);
        picturesRepository.save(pictures);
        return new ResponseEntity(HttpStatus.OK);
    }

    @PostMapping("/save")
    public ResponseEntity saveRestaurants(@RequestParam Integer pricing, @RequestParam String name, @RequestParam String description,
                                          @RequestParam String category, @RequestParam Long location, @RequestParam String address){
        Restaurant restaurant = new Restaurant();
        restaurant.setPricing(pricing);
        restaurant.setName(name);
        restaurant.setDescription(description);
        restaurant.setAddress(address);
        City city = citiesRepository.findCityById(location);
        restaurant.setCity(city);
        restaurantsRepository.save(restaurant);
        Cousine cousine = cousinesRepository.findFirstByNameAndRestaurantIsNull(category);
        restaurant = restaurantsRepository.findRestaurantByName(name);
        cousine.setRestaurant(restaurant);
        cousinesRepository.save(cousine);
        return new ResponseEntity(restaurant, HttpStatus.OK);
    }

    @GetMapping("/get/basicDetails")
    public ResponseEntity getBasicDetails(@RequestParam String name){
        Restaurant restaurant = restaurantsRepository.findRestaurantByName(name);
        return new ResponseEntity(restaurant, HttpStatus.OK);
    }

    @GetMapping("/get/logo")
    public ResponseEntity getPictures(@RequestParam Long id){
        Pictures pictures = picturesRepository.findPicturesByRestaurant_Id(id);
        return new ResponseEntity(pictures, HttpStatus.OK);
    }

    @GetMapping("/get/cousine")
    public ResponseEntity getCousine(@RequestParam Long id){
        Cousine cousine = cousinesRepository.findFirstByRestaurant_Id(id);
        return new ResponseEntity(cousine, HttpStatus.OK);
    }
}
