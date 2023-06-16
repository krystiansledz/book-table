package com.krystiansledz.booktable.controllers;

import com.krystiansledz.booktable.models.Restaurant;
import com.krystiansledz.booktable.security.services.RestaurantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/v1/restaurants")
public class RestaurantController {

    @Autowired
    private RestaurantService restaurantService;

    @GetMapping
    public ResponseEntity<List<Restaurant>> getAllRestaurants() {
        return ResponseEntity.ok(restaurantService.getAllRestaurants());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Restaurant> getTableById(@PathVariable Long id) {
        Optional<Restaurant> restaurant = restaurantService.getRestaurantById(id);
        return restaurant.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }


    // Add other endpoints as per requirement
}

