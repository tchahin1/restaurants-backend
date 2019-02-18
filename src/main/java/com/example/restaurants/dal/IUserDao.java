package com.example.restaurants.dal;

import com.example.restaurants.data.models.User;

public interface IUserDao {

    User getUnique (String email);
}
