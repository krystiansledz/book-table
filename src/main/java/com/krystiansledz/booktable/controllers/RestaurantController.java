package com.krystiansledz.booktable.controllers;

import com.krystiansledz.booktable.dto.RestaurantDTO;
import com.krystiansledz.booktable.models.Restaurant;
import com.krystiansledz.booktable.security.services.RestaurantService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

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
    public ResponseEntity<?> getRestaurantById(@PathVariable Long id) {
        try {
            Restaurant restaurant = restaurantService.getRestaurantById(id);
            RestaurantDTO restaurantDTO = new RestaurantDTO();
            BeanUtils.copyProperties(restaurant, restaurantDTO);

            return new ResponseEntity<>(restaurantDTO, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateRestaurant(@PathVariable Long id, @RequestBody RestaurantDTO restaurantDTO) {
        try {
            Restaurant restaurant = restaurantService.getRestaurantById(id);
            restaurant.setName(restaurantDTO.getName());
            restaurant.setAddress(restaurantDTO.getAddress());
            return new ResponseEntity<>(restaurantService.updateRestaurant(restaurant), HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> partialUpdateRestaurant(@PathVariable Long id, @RequestBody Map<String, Object> updates) {
        try {
            return new ResponseEntity<>(restaurantService.partialUpdateRestaurant(id, updates), HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}

