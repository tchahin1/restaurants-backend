package com.example.restaurants.controllers;

import com.example.restaurants.data.models.Restaurant;
import com.example.restaurants.data.models.Review;
import com.example.restaurants.repositories.RestaurantsRepository;
import com.example.restaurants.repositories.ReviewsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.xml.ws.Response;
import java.util.List;

@RestController
@RequestMapping("/reviews")
public class ReviewsController {
    @Autowired
    private ReviewsRepository reviewsRepository;

    @Autowired
    private RestaurantsRepository restaurantsRepository;

    @CrossOrigin
    @PostMapping
    public ResponseEntity create(@RequestBody Review review){

        //todo: validation

        Review save = reviewsRepository.save(review);

        Restaurant restaurant = review.getRestaurant();

        List<Review> reviews = reviewsRepository.findAllByRestaurant(restaurant);

        Integer sum=0;
        Integer rate;
        int i;

        for(i=0; i<reviews.size(); i++){
            sum+=reviews.get(i).getRating();
        }

        rate = sum/i;

        restaurant.setStars(rate);

        restaurantsRepository.save(restaurant);

        return new ResponseEntity("New review successfully added.", HttpStatus.OK);
    }

    @GetMapping("/check")
    public ResponseEntity check(String restaurant, String user){
        Review review = reviewsRepository.findReviewByRestaurant_NameAndUser_Email(restaurant, user);
        if(review!=null) return new ResponseEntity(review, HttpStatus.BAD_REQUEST);
        return new ResponseEntity(review, HttpStatus.OK);
    }
}
