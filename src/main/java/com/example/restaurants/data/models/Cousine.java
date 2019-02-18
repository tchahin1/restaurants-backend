package com.example.restaurants.data.models;

import javax.persistence.*;

@Entity
@Table(name = "cousines")
public class Cousine {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Restaurant restaurant;

    @Column(nullable = false)
    private String name;

    public long getId() {
        return id;
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }
}
