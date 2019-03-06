package com.example.restaurants.controllers;

import com.example.restaurants.data.models.Review;
import com.example.restaurants.repositories.ReviewsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/reviews")
public class ReviewsController {
    @Autowired
    private ReviewsRepository reviewsRepository;

    @CrossOrigin
    @PostMapping
    public ResponseEntity create(@RequestBody Review review){

        //todo: validation

        Review save = reviewsRepository.save(review);

        return new ResponseEntity("New review successfully added.", HttpStatus.OK);
    }

    @GetMapping("/check")
    public Review check(String restaurant, String user){
        Review review = reviewsRepository.findReviewByRestaurant_NameAndUser_Email(restaurant, user);
        if(review!=null) return review;
        return review;
    }
}