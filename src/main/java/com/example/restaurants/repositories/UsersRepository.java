package com.example.restaurants.repositories;

import com.example.restaurants.data.models.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;
import java.util.UUID;

public interface UsersRepository extends CrudRepository<Users, Long> {
    Users findByEmail(String email);

    void deleteByEmail(String email);

    List<Users> findByNameContainingIgnoreCaseOrEmailContainingIgnoreCase(String query, String query1);

    Users findUsersById(Long num);

    List<Users> findAllByCity_Id(Long num);
}
