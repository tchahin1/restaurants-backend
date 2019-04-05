package com.example.restaurants.repositories;

import com.example.restaurants.data.models.Pictures;
import org.springframework.data.repository.CrudRepository;

public interface PicturesRepository extends CrudRepository<Pictures, Long> {
    Pictures findPicturesByRestaurant_Id(Long num);

    Pictures findPicturesById(Long num);
}
