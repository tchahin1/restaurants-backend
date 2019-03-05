package com.example.restaurants.controllers;

import com.example.restaurants.data.models.Tables;
import com.example.restaurants.repositories.TablesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/tables")
public class TablesController {

    @Autowired
    private TablesRepository tablesRepository;

    @GetMapping(value = "/restaurant")
    public long getRestaurant(String name, Integer type){
        long tables = tablesRepository.countTablesByReservedIsFalseAndRestaurant_NameAndType(name, type);
        return tables;
    }

    @GetMapping(value = "/type")
    public ResponseEntity getTableTypes(String name){
        List<Tables> tables = tablesRepository.findDistinctByRestaurant_Name(name);
        return new ResponseEntity(tables, HttpStatus.OK);
    }

}
