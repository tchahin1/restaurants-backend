package com.example.restaurants.controllers;

//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.example.restaurants.dal.impl.CityDao;
import com.example.restaurants.dal.impl.UserDao;
import com.example.restaurants.data.dtos.UserDTO;
import com.example.restaurants.data.dtos.UserLoginDTO;
import com.example.restaurants.data.models.City;
import com.example.restaurants.data.models.Users;
import com.example.restaurants.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserDao userDao;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CityDao cityDao;

    //jwt

    //private UserRepository userRepository;
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public UserController(UserRepository userRepository,
                          BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @PostMapping("/sign-up")
    public void signUp(@RequestBody Users user) {

        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    //kraj jwt

    /*@PostMapping("/login")
    public ResponseEntity login(@RequestBody UserLoginDTO user) {

        if (StringUtils.isEmpty(user.getEmail())) {
            return null;
        }

//        Users existingUser = userDao.getUnique(user.getEmail());
        Users existingUser = userRepository.findByEmail(user.getEmail());

        if (existingUser != null) {
            //throw new IllegalArgumentException("Users already exists.");
            return ResponseEntity.ok(existingUser);
        }

        //samo jos testirati validaciju passworda!
        return new ResponseEntity(HttpStatus.BAD_REQUEST);
    }*/

    @PostMapping("/register")
    public ResponseEntity register(@RequestBody UserDTO userDTO) {

        // todo: remove, not needed because you already have a unique db constraint
//        Users existingUser = userDao.getUnique(userDTO.getEmail());
//
//        if (existingUser != null) {
//            throw new IllegalArgumentException("Users already exists.");
//        }

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
        return new ResponseEntity(HttpStatus.OK);
    }
}
