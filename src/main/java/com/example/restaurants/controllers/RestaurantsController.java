package com.example.restaurants.controllers;

import com.example.restaurants.dal.impl.RestaurantDao;
import com.example.restaurants.data.models.*;
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

    @Autowired
    private TablesRepository tablesRepository;

    @Autowired
    private ReservationsRepository reservationsRepository;

    @Autowired
    private ReviewsRepository reviewsRepository;

    @Autowired
    private LocationsRepository locationsRepository;

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
        if(query.equals("")) query = "sarajevo";
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
        Restaurant restaurant = restaurantsRepository.findFirstByName(name);
        List<Tables> tables = tablesRepository.findAllByRestaurant(restaurant);
        for(int i=0; i<tables.size(); i++){
            List<Reservations> reservations = reservationsRepository.findAllByTable_Id(tables.get(i).getId());
            for(int j=0; j<reservations.size(); j++){
                reservationsRepository.delete(reservations.get(j));
            }
            tablesRepository.delete(tables.get(i));
        }
        Pictures pictures = picturesRepository.findPicturesByRestaurant_Id(restaurant.getId());
        if(pictures!=null) {
            pictures.setRestaurant(null);
            picturesRepository.delete(pictures);
        }
        List<Cousine> cousine = cousinesRepository.findAllByRestaurant_Id(restaurant.getId());
        for(int i=0; i<cousine.size(); i++){
            cousine.get(i).setRestaurant(null);
        }
        List<Review> review = reviewsRepository.findAllByRestaurant(restaurant);
        for(int i=0; i<review.size(); i++){
            reviewsRepository.delete(review.get(i));
        }
        Locations locations = locationsRepository.findByRestaurant_Id(restaurant.getId());
        if(locations!=null) {
            locations.setRestaurant(null);
            locationsRepository.delete(locations);
        }
        restaurantsRepository.delete(restaurant);
        return new ResponseEntity(HttpStatus.OK);
    }

    @GetMapping(value = "/get/search")
    public ResponseEntity getRestaurants(@RequestParam String name){
        return new ResponseEntity(restaurantsRepository.findByNameContainingIgnoreCase(name),HttpStatus.OK);
    }

    @PostMapping("/saveLogo")
    public ResponseEntity saveLogo(@RequestParam String pictureUrl, @RequestParam Long id){
        Restaurant restaurant = restaurantsRepository.findRestaurantById(id);
        Pictures pictures = picturesRepository.findPicturesByRestaurant_Id(id);
        if(pictures!=null && !(pictures.getLogoUrl().equals(pictureUrl))){
            pictures.setLogoUrl(pictureUrl);
            restaurant.setPictureUrl(pictureUrl);
            picturesRepository.save(pictures);
            pictures = picturesRepository.findPicturesByRestaurant_Id(id);
            return new ResponseEntity(pictures, HttpStatus.OK);
        }
        else if(pictures!=null && pictures.getLogoUrl().equals(pictureUrl)){
            return new ResponseEntity(pictures, HttpStatus.OK);
        }
        else {
            pictures = new Pictures();
            pictures.setLogoUrl(pictureUrl);
            pictures.setRestaurant(restaurant);
            picturesRepository.save(pictures);
            pictures = picturesRepository.findPicturesByRestaurant_Id(id);
            restaurant.setPictureUrl(pictureUrl);
            restaurantsRepository.save(restaurant);
            return new ResponseEntity(pictures, HttpStatus.OK);
        }
    }

    @PostMapping("/saveCover")
    public ResponseEntity saveCover(@RequestParam String pictureUrl, @RequestParam Long id){
        Pictures pictures = picturesRepository.findPicturesById(id);
        if(pictures!=null && pictures.getCoverUrl()==null){
            pictures.setCoverUrl(pictureUrl);
            picturesRepository.save(pictures);
            return new ResponseEntity(HttpStatus.OK);
        }

        else if(pictures!=null && !(pictures.getCoverUrl().equals(pictureUrl))){
            pictures.setCoverUrl(pictureUrl);
            picturesRepository.save(pictures);
            return new ResponseEntity(HttpStatus.OK);
        }
        else if(pictures!=null && pictures.getCoverUrl().equals(pictureUrl)){
            return new ResponseEntity(HttpStatus.OK);
        }
        else {
            pictures.setCoverUrl(pictureUrl);
            picturesRepository.save(pictures);
            return new ResponseEntity(HttpStatus.OK);
        }
    }

    @PostMapping("/save")
    public ResponseEntity saveRestaurants(@RequestParam Integer pricing, @RequestParam String name, @RequestParam String description,
                                          @RequestParam String category, @RequestParam Long location, @RequestParam String address){
        Restaurant restaurant = new Restaurant();
        restaurant.setPricing(pricing);
        restaurant.setName(name);
        restaurant.setStars(0);
        restaurant.setDescription(description);
        restaurant.setAddress(address);
        City city = citiesRepository.findCityById(location);
        restaurant.setCity(city);
        restaurantsRepository.save(restaurant);
        Cousine cousine = cousinesRepository.findFirstByNameAndRestaurantIsNull(category);
        if(cousine == null){
            cousine = new Cousine();
            cousine.setName(category);
            cousinesRepository.save(cousine);
            cousine = cousinesRepository.findFirstByNameAndRestaurantIsNull(category);
        }
        restaurant = restaurantsRepository.findRestaurantByName(name);
        cousine.setRestaurant(restaurant);
        cousinesRepository.save(cousine);
        return new ResponseEntity(restaurant, HttpStatus.OK);
    }

    @PostMapping("/edit")
    public ResponseEntity editRestaurants(@RequestParam Long id, @RequestParam Integer pricing, @RequestParam String name, @RequestParam String description,
                                          @RequestParam String category, @RequestParam Long location, @RequestParam String address){
        Restaurant restaurant = restaurantsRepository.findRestaurantById(id);
        if(restaurant.getAddress().equals(address)) {
            restaurant.setPricing(pricing);
            restaurant.setName(name);
            restaurant.setStars(0);
            restaurant.setDescription(description);
            restaurantsRepository.save(restaurant);
            Cousine cousine = cousinesRepository.findFirstByNameAndRestaurantIsNull(category);
            if(cousine == null){
                cousine = new Cousine();
                cousine.setName(category);
                cousinesRepository.save(cousine);
                cousine = cousinesRepository.findFirstByNameAndRestaurantIsNull(category);
            }
            restaurant = restaurantsRepository.findRestaurantByName(name);
            cousine.setRestaurant(restaurant);
            cousinesRepository.save(cousine);
            return new ResponseEntity(HttpStatus.OK);
        }
        else {
            restaurant = new Restaurant();
            restaurant.setPricing(pricing);
            restaurant.setName(name);
            restaurant.setStars(0);
            restaurant.setDescription(description);
            restaurant.setAddress(address);
            City city = citiesRepository.findCityById(location);
            restaurant.setCity(city);
            restaurantsRepository.save(restaurant);
            Cousine cousine = cousinesRepository.findFirstByNameAndRestaurantIsNull(category);
            if(cousine == null){
                cousine = new Cousine();
                cousine.setName(category);
                cousinesRepository.save(cousine);
                cousine = cousinesRepository.findFirstByNameAndRestaurantIsNull(category);
            }
            restaurant = restaurantsRepository.findRestaurantByName(name);
            cousine.setRestaurant(restaurant);
            cousinesRepository.save(cousine);
            return new ResponseEntity(restaurant, HttpStatus.OK);
        }
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

    @PostMapping("/save/tables")
    public ResponseEntity saveTables(@RequestParam Long id, @RequestParam List<Integer> typeArray, @RequestParam List<Integer> ammountArray){
        Restaurant restaurant = restaurantsRepository.findRestaurantById(id);
        for(int i=0; i<ammountArray.size(); i++){
            for(int j=0; j<ammountArray.get(i); j++){
                Tables tables = new Tables();
                tables.setType(typeArray.get(i));
                tables.setRestaurant(restaurant);
                tables.setReserved(false);
                tablesRepository.save(tables);
            }
        }
        return new ResponseEntity(HttpStatus.OK);
    }

    @GetMapping("/get/tables")
    public ResponseEntity getTables(@RequestParam Long id){
        List<Tables> tables = tablesRepository.findTablesByRestaurant_Id(id);
        return new ResponseEntity(tables, HttpStatus.OK);
    }

    @PostMapping("/edit/tables")
    public ResponseEntity editTables(@RequestParam Long id, @RequestParam List<Integer> typeArray, @RequestParam List<Integer> ammountArray){
        Restaurant restaurant = restaurantsRepository.findRestaurantById(id);
        for(int i=0; i<ammountArray.size(); i++){
            for(int j=0; j<ammountArray.get(i); j++){
                ArrayList<Tables> existingTables = tablesRepository.findTablesByTypeAndRestaurant_Id(typeArray.get(i), id);
                if(existingTables!=null && existingTables.size()!=0 && existingTables.size()<ammountArray.get(i)){
                    int k=0;
                    while(k<existingTables.size()) {
                        j++;
                        k++;
                    }
                    while(k<ammountArray.get(i)){
                        Tables tables = new Tables();
                        tables.setType(typeArray.get(i));
                        tables.setRestaurant(restaurant);
                        tables.setReserved(false);
                        tablesRepository.save(tables);
                        k++;
                    }
                    break;
                }
                else if(existingTables!=null && existingTables.size()!=0 && existingTables.size()>ammountArray.get(i)){
                    int k=0;
                    while(k<ammountArray.get(i)) k++;
                    while(k<existingTables.size()){
                        Tables table = tablesRepository.findFirstByTypeAndRestaurant_Id(typeArray.get(i), id);
                        tablesRepository.delete(table);
                        k++;
                        j++;
                    }
                    break;
                }
                else if(existingTables!=null && existingTables.size()!=0 && existingTables.size()==ammountArray.get(i)){
                    while(j<existingTables.size()) j++;
                }
                else {
                    Tables tables = new Tables();
                    tables.setType(typeArray.get(i));
                    tables.setRestaurant(restaurant);
                    tables.setReserved(false);
                    tablesRepository.save(tables);
                }
            }
        }
        return new ResponseEntity(HttpStatus.OK);
    }

    @GetMapping("/sort")
    public ResponseEntity sort(String query, Double lon, Double lat, Integer pricing, Integer rating) {

        if (pricing == null) {
            pricing = -1;
        }

        if (rating == null) {
            rating = -1;
        }

        List<Restaurant> result = restaurantsRepository.sortRestaurants(lat, lon, query, pricing, rating);
        return new ResponseEntity(result, HttpStatus.OK);
    }
}
