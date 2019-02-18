package com.example.restaurants.controllers;

import com.example.restaurants.dal.impl.CountryDao;
import com.example.restaurants.data.models.Country;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/countries")
public class CountryController {

    @Autowired
    private CountryDao countryDao;

    @CrossOrigin
    @RequestMapping(value = "/all", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public List<Country> getAllCountries() {
        return countryDao.getAll();
    }
}
