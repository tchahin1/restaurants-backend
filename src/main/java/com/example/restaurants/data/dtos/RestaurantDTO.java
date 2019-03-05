package com.example.restaurants.data.dtos;


//TODO: CREATE A CONSTRUCTOR THAT ACCEPTS RESTAURANTS AND CONVERT IT TO RESTAURANTDTO

public class RestaurantDTO {
    private String pictureUrl;
    private Integer stars;
    private Integer pricing;
    private String name;
    private String description;
    private Integer reservationInterval;

    public Integer getReservationInterval() {
        return reservationInterval;
    }

    public void setReservationInterval(Integer reservationInterval) {
        this.reservationInterval = reservationInterval;
    }

    public String getPictureUrl() {
        return pictureUrl;
    }

    public void setPictureUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl;
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
}
