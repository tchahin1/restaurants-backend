package com.example.restaurants.repositories;

import com.example.restaurants.data.models.Review;
import org.springframework.data.repository.CrudRepository;

public interface ReviewsRepository extends CrudRepository<Review, Long> {
    Review findReviewByRestaurant_NameAndUser_Email(String restaurant, String user);
}
