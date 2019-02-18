package com.example.restaurants.controllers;

//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.example.restaurants.dal.impl.CityDao;
import com.example.restaurants.dal.impl.UserDao;
import com.example.restaurants.data.dtos.UserDTO;
import com.example.restaurants.data.dtos.UserLoginDTO;
import com.example.restaurants.data.models.City;
import com.example.restaurants.data.models.User;
import com.example.restaurants.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

//    private UserRepository applicationUserRepository;
//    private BCryptPasswordEncoder bCryptPasswordEncoder;
//
//    public UserController(UserRepository applicationUserRepository,
//                          BCryptPasswordEncoder bCryptPasswordEncoder) {
//        this.applicationUserRepository = applicationUserRepository;
//        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
//    }
//
//    @PostMapping("/sign-up")
//    public void signUp(@RequestBody User user) {
//        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
//        applicationUserRepository.save(user);
//    }

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody UserLoginDTO user) {

        if (StringUtils.isEmpty(user.getEmail())) {
            return null;
        }

//        User existingUser = userDao.getUnique(user.getEmail());
        User existingUser = userRepository.findByEmail(user.getEmail());

        if (existingUser != null) {
            //throw new IllegalArgumentException("User already exists.");
            return ResponseEntity.ok(existingUser);
        }

        //samo jos testirati validaciju passworda!
        return new ResponseEntity(HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/register")
    public User register(@RequestBody UserDTO userDTO) {

        // todo: remove, not needed because you already have a unique db constraint
//        User existingUser = userDao.getUnique(userDTO.getEmail());
//
//        if (existingUser != null) {
//            throw new IllegalArgumentException("User already exists.");
//        }

        User user = new User();
        user.setName(userDTO.getName());
        user.setLastName(userDTO.getLastName());
        user.setEmail(userDTO.getEmail());
        user.setPhoneNumber(userDTO.getPhoneNumber());
        user.setPassword(userDTO.getPassword());

        City city = cityDao.getCity(userDTO.getCity(), userDTO.getCountry());
        user.setCity(city);
        user.setCountry(city.getCountry());

        return userDao.save(user);
    }
}
