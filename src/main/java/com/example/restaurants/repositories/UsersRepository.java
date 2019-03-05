package com.example.restaurants.repositories;

import com.example.restaurants.data.models.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.UUID;

public interface UsersRepository extends CrudRepository<Users, Long> {
    Users findByEmail(String email);
}
