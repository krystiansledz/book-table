package com.krystiansledz.booktable.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.krystiansledz.booktable.models.Restaurant;
import com.krystiansledz.booktable.models.RestaurantTable;

import java.time.LocalDateTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ReservationDTO {
    private Long id;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
    private Long customer_id;
    private Long restaurantTable_id;
    private RestaurantTable restaurantTable;
    private Restaurant restaurant;
    private Integer rating;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getStartDateTime() {
        return startDateTime;
    }

    public void setStartDateTime(LocalDateTime startDateTime) {
        this.startDateTime = startDateTime;
    }

    public LocalDateTime getEndDateTime() {
        return endDateTime;
    }

    public void setEndDateTime(LocalDateTime endDateTime) {
        this.endDateTime = endDateTime;
    }

    public Long getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(Long customer_id) {
        this.customer_id = customer_id;
    }

    public Long getRestaurantTable_id() {
        return restaurantTable_id;
    }

    public void setRestaurantTable_id(Long restaurantTable_id) {
        this.restaurantTable_id = restaurantTable_id;
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }

    public RestaurantTable getRestaurantTable() {
        return restaurantTable;
    }

    public void setRestaurantTable(RestaurantTable restaurantTable) {
        this.restaurantTable = restaurantTable;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        if (rating < 1 || rating > 5) {
            throw new IllegalArgumentException("Rating must be between 1 and 5");
        }
        this.rating = rating;
    }

    @Override
    public String toString() {
        return "ReservationDTO{" +
                "id=" + id +
                ", startDateTime=" + startDateTime +
                ", endDateTime=" + endDateTime +
                ", customer_id=" + customer_id +
                ", restaurantTable_id=" + restaurantTable_id +
                ", restaurantTable=" + restaurantTable +
                ", restaurant=" + restaurant +
                ", rating=" + rating +
                '}';
    }
}
