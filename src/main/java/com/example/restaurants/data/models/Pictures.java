package com.example.restaurants.data.models;

import javax.persistence.*;

@Entity
@Table(name = "pictures")
public class Pictures {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String logoUrl;

    @Column
    private String coverUrl;

    @OneToOne
    private Restaurant restaurant;

    public Long getId() {
        return id;
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public void setLogoUrl(String url) {
        this.logoUrl = url;
    }

    public String getCoverUrl() {
        return coverUrl;
    }

    public void setCoverUrl(String url) {
        this.coverUrl = url;
    }

    public Restaurant getRestaurant()  {
        return restaurant;
    }

    public void setRestaurant(Restaurant restaurant)  {
        this.restaurant = restaurant;
    }
}

