package com.krystiansledz.booktable.models;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.krystiansledz.booktable.enums.UserType;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "restaurants",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "email")
        })
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Restaurant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 50)
    @Email
    private String email;

    @NotBlank
    @Size(max = 120)
    @JsonIgnore
    private String password;

    @NotBlank
    @Size(max = 40)
    private String name;

    @Enumerated(EnumType.STRING)
    @JsonIgnore
    private UserType userType = UserType.RESTAURANT;

    @Size(max = 120)
    private String address;

    @OneToMany(mappedBy = "restaurant", cascade = CascadeType.ALL)
    @JsonManagedReference(value = "restaurant-restaurantTables")
    private List<RestaurantTable> restaurantTables = new ArrayList<>();

    @OneToMany(mappedBy = "restaurant", cascade = CascadeType.ALL)
    @JsonManagedReference(value = "restaurant-businessHours")
    private List<BusinessHours> businessHours = new ArrayList<>();

    public Restaurant() {
    }

    public Restaurant(String email, String password, String name) {
        this.email = email;
        this.password = password;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public UserType getUserType() {
        return userType;
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
