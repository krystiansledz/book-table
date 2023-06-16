package com.krystiansledz.booktable.dto;

import com.krystiansledz.booktable.models.Restaurant;

import java.time.DayOfWeek;
import java.time.LocalTime;

public class BusinessHoursDTO {
    private Long id;
    private DayOfWeek dayOfWeek;
    private LocalTime openingTime;
    private LocalTime closingTime;
    private Long restaurant_id;

    public DayOfWeek getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(DayOfWeek dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public LocalTime getOpeningTime() {
        return openingTime;
    }

    public void setOpeningTime(LocalTime openingTime) {
        this.openingTime = openingTime;
    }

    public LocalTime getClosingTime() {
        return closingTime;
    }

    public void setClosingTime(LocalTime closingTime) {
        this.closingTime = closingTime;
    }

    public Long getRestaurant_id() {
        return restaurant_id;
    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant_id = restaurant.getId();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
