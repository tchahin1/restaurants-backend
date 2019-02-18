package com.example.restaurants.data.models;

import javax.persistence.*;

@Entity
@Table(name = "restaurants")
public class Restaurant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column
    private Integer stars;

    @Column
    private Integer pricing;

    @Column
    private String name;

    @Column
    private String description;

    @Column
    private Integer reservationInterval;

    public long getId() {
        return id;
    }

    public Integer getStars() {
        return stars;
    }

    public void setStars(Integer stars) {
        this.stars = stars;
    }

    public Integer getPricing() {
        return pricing;
    }

    public void setPricing(Integer pricing) {
        this.pricing = pricing;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getReservationInterval() {
        return reservationInterval;
    }

    public void setReservationInterval(Integer reservationInterval) {
        this.reservationInterval = reservationInterval;
    }
}
