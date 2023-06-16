package com.krystiansledz.booktable.dto;

import com.krystiansledz.booktable.models.BusinessHours;
import com.krystiansledz.booktable.models.RestaurantTable;

import java.util.List;

public class RestaurantDTO {
    private Long id;
    private String email;
    private String name;
    private String address;
    private List<RestaurantTable> restaurantTables;
    private List<BusinessHours> businessHours;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public List<RestaurantTable> getRestaurantTables() {
        return restaurantTables;
    }

    public List<BusinessHours> getBusinessHours() {
        return businessHours;
    }
}
