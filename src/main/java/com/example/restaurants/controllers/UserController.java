package com.example.restaurants.controllers;

//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.example.restaurants.dal.impl.CityDao;
import com.example.restaurants.dal.impl.UserDao;
import com.example.restaurants.data.dtos.UserDTO;
import com.example.restaurants.data.models.City;
import com.example.restaurants.data.models.Country;
import com.example.restaurants.data.models.Users;
import com.example.restaurants.repositories.CitiesRepository;
import com.example.restaurants.repositories.CountriesRepository;
import com.example.restaurants.repositories.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserDao userDao;

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private CityDao cityDao;

    @Autowired
    private CountriesRepository countriesRepository;

    @Autowired
    private CitiesRepository citiesRepository;

    //private UsersRepository usersRepository;
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public UserController(UsersRepository usersRepository,
                          BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.usersRepository = usersRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @PostMapping("/sign-up")
    public void signUp(@RequestBody Users user) {

        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        usersRepository.save(user);
    }

    @GetMapping("/id")
    public ResponseEntity getId(@RequestParam String username){
        return new ResponseEntity(usersRepository.findByEmail(username), HttpStatus.OK);
    }

    @GetMapping("/all")
    public ResponseEntity getUsers(){
        return new ResponseEntity(usersRepository.findAll(), HttpStatus.OK);
    }

    @PostMapping("/register")
    public ResponseEntity register(@RequestBody UserDTO userDTO) {

        Users existingUser = usersRepository.findByEmail(userDTO.getEmail());
        if(existingUser!=null){
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }

        else {
            Users user = new Users();
            user.setName(userDTO.getName());
            user.setLastName(userDTO.getLastName());
            user.setEmail(userDTO.getEmail());
            user.setPhoneNumber(userDTO.getPhoneNumber());
            user.setPassword(bCryptPasswordEncoder.encode(userDTO.getPassword()));

            System.out.println(userDTO.getCity() + userDTO.getCountry());
            City city = cityDao.getCity(userDTO.getCity(), userDTO.getCountry());
            user.setCity(city);
            user.setCountry(city.getCountry());
            userDao.save(user);
            return new ResponseEntity(user, HttpStatus.OK);
        }
    }

    @GetMapping("/count")
    @ResponseStatus(HttpStatus.OK)
    public long usersCount(){
        return usersRepository.count();
    }

    @Transactional
    @GetMapping(value = "/delete")
    public ResponseEntity deleteUser(@RequestParam String email) {
        usersRepository.deleteByEmail(email);
        return new ResponseEntity(HttpStatus.OK);
    }

    @GetMapping(value = "/get")
    public ResponseEntity getUser(@RequestParam String email){
        return new ResponseEntity(usersRepository.findByEmail(email),HttpStatus.OK);
    }

    @GetMapping(value = "/get/search")
    public ResponseEntity getUsers(@RequestParam String query){
        return new ResponseEntity(usersRepository.findByNameContainingIgnoreCaseOrEmailContainingIgnoreCase(query, query),HttpStatus.OK);
    }

    @GetMapping("/edit")
    public ResponseEntity edit(@RequestParam Long id, @RequestParam String name, @RequestParam String last_name, @RequestParam String email, @RequestParam String phone_num, @RequestParam Long country, @RequestParam Long city){
        Users user = usersRepository.findUsersById(id);
        if(!(user.getEmail().equals(email))){
            Users existingUser = usersRepository.findByEmail(email);
            if(existingUser!=null){
                user.setName(name);
                user.setLastName(last_name);
                user.setEmail(email);
                user.setPhoneNumber(phone_num);
                Country existingCountry = countriesRepository.findCountryById(country);
                City existingCity = citiesRepository.findCityById(city);
                user.setCity(existingCity);
                user.setCountry(existingCountry);
                usersRepository.save(user);
                return new ResponseEntity(HttpStatus.OK);
            }
            else return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
        else{
            user.setName(name);
            user.setLastName(last_name);
            user.setPhoneNumber(phone_num);
            Country existingCountry = countriesRepository.findCountryById(country);
            City existingCity = citiesRepository.findCityById(city);
            user.setCity(existingCity);
            user.setCountry(existingCountry);
            usersRepository.save(user);
            return new ResponseEntity(HttpStatus.OK);
        }
    }
}
