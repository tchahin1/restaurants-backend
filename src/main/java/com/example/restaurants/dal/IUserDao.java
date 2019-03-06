package com.example.restaurants.dal;

import com.example.restaurants.data.models.Users;

public interface IUserDao {

    Users getUnique (String email);
}
