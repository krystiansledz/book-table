package com.krystiansledz.booktable.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.krystiansledz.booktable.models.Reservation;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)

public class RestaurantTableDTO {
    private Long id;
    private Integer number;
    private Integer capacity;
    private Long restaurant_id;
    private List<Reservation> reservations;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    public Long getRestaurant_id() {
        return restaurant_id;
    }

    public void setRestaurant_id(Long restaurant_id) {
        this.restaurant_id = restaurant_id;
    }

    public List<Reservation> getReservations() {
        return reservations;
    }

    public void setReservations(List<Reservation> reservations) {
        this.reservations = reservations;
    }
}
