package com.example.restaurants.tests;

import com.example.restaurants.controllers.ReviewsController;
import com.example.restaurants.data.models.Restaurant;
import com.example.restaurants.data.models.Review;
import com.example.restaurants.data.models.Users;
import com.example.restaurants.repositories.RestaurantsRepository;
import com.example.restaurants.repositories.ReviewsRepository;
import com.example.restaurants.repositories.UsersRepository;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.testng.Assert;
import org.testng.annotations.Test;


@RunWith(SpringRunner.class)
@DataJpaTest
public class ReviewsTests {

    @Autowired
    ReviewsController reviewsController;

    @Autowired
    RestaurantsRepository restaurantsRepository;

    @Autowired
    UsersRepository usersRepository;

    @Test
    public void create_NewReviewForExistingUser_ReturnsResponseEntity(){
        final ResponseEntity expected = new ResponseEntity("New review successfully added.", HttpStatus.OK);


        final Review review = new Review();
        review.setComment("This is a nice restaurant");
        review.setRating(4);

        final Long id = new Long(5);
        final Restaurant restaurant = restaurantsRepository.findRestaurantById(id);
        review.setRestaurant(restaurant);

        final Long userId = new Long(2);
        final Users users = usersRepository.findUsersById(userId);
        review.setUser(users);

        final ResponseEntity actual = reviewsController.create(review);


        Assert.assertEquals(expected, actual);
    }

    @Test
    public void check_ReviewsForUserWhoHasReviews_ReturnsBAD_REQUEST(){
        final ResponseEntity expected = new ResponseEntity(HttpStatus.BAD_REQUEST);


        final Long id = new Long(5);
        final Restaurant restaurant = restaurantsRepository.findRestaurantById(id);

        final Long userId = new Long(2);
        final Users users = usersRepository.findUsersById(userId);

        final ResponseEntity actual = reviewsController.check(restaurant.getName(), users.getEmail());


        Assert.assertEquals(expected.getStatusCode(), actual.getStatusCode());
    }

    @Test
    public void check_ReviewsForUserWhoHasNoReviews_ReturnsOK(){
        final ResponseEntity expected = new ResponseEntity(HttpStatus.OK);


        final Long id = new Long(5);
        final Restaurant restaurant = restaurantsRepository.findRestaurantById(id);

        final Long userId = new Long(2);
        final Users users = usersRepository.findUsersById(userId);

        final ResponseEntity actual = reviewsController.check(restaurant.getName(), users.getEmail());


        Assert.assertEquals(expected.getStatusCode(), actual.getStatusCode());
    }
}
