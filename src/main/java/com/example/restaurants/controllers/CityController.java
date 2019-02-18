package com.example.restaurants.controllers;

import com.example.restaurants.dal.impl.CityDao;
import com.example.restaurants.data.models.City;
import com.example.restaurants.data.models.Country;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/cities")
public class CityController {

    @Autowired
    private CityDao cityDao;

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
}
