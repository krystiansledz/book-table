package com.krystiansledz.booktable.controllers;

import com.krystiansledz.booktable.dto.RestaurantDTO;
import com.krystiansledz.booktable.models.Reservation;
import com.krystiansledz.booktable.models.Restaurant;
import com.krystiansledz.booktable.security.services.ReservationService;
import com.krystiansledz.booktable.security.services.RestaurantService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/v1/restaurants")
public class RestaurantController {

    @Autowired
    private RestaurantService restaurantService;
    @Autowired
    private ReservationService reservationService;

    @GetMapping
    public ResponseEntity<List<RestaurantDTO>> getAllRestaurants(@RequestParam(required = false) String name, @RequestParam(required = false) String sortBy,
                                                                 @RequestParam(required = false) String direction) {
        List<Restaurant> restaurants = restaurantService.getAllRestaurants(name, sortBy, direction);


        List<RestaurantDTO> restaurantDTOs = restaurants.stream().map(restaurant -> {
            RestaurantDTO restaurantDTO = new RestaurantDTO();

            BeanUtils.copyProperties(restaurant, restaurantDTO);

            Double averageRating = restaurantService.calculateAverageRating(restaurant);
            restaurantDTO.setRating(averageRating);

            return restaurantDTO;
        }).collect(Collectors.toList());

        return ResponseEntity.ok(restaurantDTOs);
    }


    @GetMapping("/{id}")
    public ResponseEntity<?> getRestaurantById(@PathVariable Long id) {
        try {
            Restaurant restaurant = restaurantService.getRestaurantById(id);
            RestaurantDTO restaurantDTO = new RestaurantDTO();
            BeanUtils.copyProperties(restaurant, restaurantDTO);

            Double averageRating = restaurantService.calculateAverageRating(restaurant);
            restaurantDTO.setRating(averageRating);

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

    @GetMapping("/{id}/available-tables")
    public ResponseEntity<?> getRestaurantAvailableTables(@PathVariable Long id, @RequestParam LocalDateTime start, @RequestParam LocalDateTime end) {
        try {
            return new ResponseEntity<>(restaurantService.getRestaurantAvailableTables(id, start, end), HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/{id}/ratings")
    public ResponseEntity<Map<Integer, Long>> getRestaurantRatings(@PathVariable Long id) {
        try {
            Restaurant restaurant = restaurantService.getRestaurantById(id);

            Map<Integer, Long> ratingCounts = new HashMap<>();
            for (int i = 1; i <= 5; i++) {
                ratingCounts.put(i, 0L);
            }

            List<Reservation> reservations = reservationService.getAllReservationsByRestaurant(restaurant.getId());

            reservations.stream()
                    .map(Reservation::getRating)
                    .filter(Objects::nonNull)
                    .forEach(rating -> ratingCounts.put(rating, ratingCounts.get(rating) + 1));

            return ResponseEntity.ok(ratingCounts);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

}

