package com.example.restaurants.controllers;

import com.example.restaurants.dal.impl.CityDao;
import com.example.restaurants.data.models.City;
import com.example.restaurants.data.models.Country;
import com.example.restaurants.repositories.CitiesRepository;
import com.example.restaurants.repositories.CountriesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/cities")
public class CitiesController {

    @Autowired
    private CityDao cityDao; // repo

    @Autowired
    private CitiesRepository citiesRepository;

    @Autowired
    private CountriesRepository countriesRepository;

    @CrossOrigin
    @RequestMapping(value = "/all", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public List<City> getAllCities(){
        return cityDao.getAll();
    }

    @CrossOrigin
    @RequestMapping(value = "/country", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public List<City> getCountryCities(@RequestBody Country country){
        return cityDao.getCityByCountry(country.getName(), country.getId());
    }

    @CrossOrigin
    @GetMapping("/count")
    @ResponseStatus(HttpStatus.OK)
    public long getCitiesCount(){
        return citiesRepository.count();
    }

    @Transactional
    @GetMapping(value = "/delete")
    public ResponseEntity deleteCity(@RequestParam String name) {
        citiesRepository.deleteByName(name);
        return new ResponseEntity(HttpStatus.OK);
    }

    @GetMapping(value = "/get")
    public ResponseEntity getCity(@RequestParam String name){
        return new ResponseEntity(citiesRepository.findByName(name), HttpStatus.OK);
    }

    @GetMapping(value = "/get/search")
    public ResponseEntity getCities(@RequestParam String name){
        return new ResponseEntity(citiesRepository.findByNameContainingIgnoreCase(name),HttpStatus.OK);
    }

    @GetMapping(value = "/save")
    public ResponseEntity saveCity(@RequestParam String city, @RequestParam String country){
        City existingCity = citiesRepository.findByName(city);
        if(existingCity!=null) return new ResponseEntity(HttpStatus.BAD_REQUEST);
        City cities = new City();
        Country existingCountry = countriesRepository.findCountryByNameContainingIgnoreCaseOrNameIsLikeIgnoreCase(country, country);
        if(existingCountry == null){
            Country countryTmp = new Country();
            countryTmp.setName(country);
            countriesRepository.save(countryTmp);
            countryTmp = countriesRepository.findByName(country);
            cities.setName(city);
            cities.setCountry(countryTmp);
            citiesRepository.save(cities);
            return new ResponseEntity(HttpStatus.OK);
        }
        else{
            cities.setName(city);
            cities.setCountry(existingCountry);
            citiesRepository.save(cities);
            return new ResponseEntity(HttpStatus.OK);
        }
    }

    @GetMapping(value = "/edit")
    public ResponseEntity editCity(@RequestParam String name, @RequestParam Long id){
        City existingCity = citiesRepository.findCityById(id);
        if(!(existingCity.getName().equals(name))) {
            City cityTmp = citiesRepository.findByName(name);
            if(cityTmp==null) {
                existingCity.setName(name);
                citiesRepository.save(existingCity);
                return new ResponseEntity(HttpStatus.OK);
            }
            else return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity(HttpStatus.OK);
    }
}
