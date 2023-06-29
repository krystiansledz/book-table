package com.krystiansledz.booktable.controllers;

import com.krystiansledz.booktable.dto.ReservationDTO;
import com.krystiansledz.booktable.models.Customer;
import com.krystiansledz.booktable.models.Reservation;
import com.krystiansledz.booktable.models.RestaurantTable;
import com.krystiansledz.booktable.security.jwt.JwtUtils;
import com.krystiansledz.booktable.security.services.CustomerService;
import com.krystiansledz.booktable.security.services.ReservationService;
import com.krystiansledz.booktable.security.services.RestaurantTableService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/v1/restaurants/{restaurantId}/reservations")
public class RestaurantReservationController {

    @Autowired
    JwtUtils jwtUtils;
    @Autowired
    private ReservationService reservationService;
    @Autowired
    private CustomerService customerService;
    @Autowired
    private RestaurantTableService restaurantTableService;

    @GetMapping
    public ResponseEntity<List<Reservation>> getAllReservations(@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date, @PathVariable Long restaurantId) {
        if (date != null) {
            return ResponseEntity.ok(reservationService.getReservationsByDate(String.valueOf(date), restaurantId));
        } else {
            return ResponseEntity.ok(reservationService.getAllReservationsByRestaurant(restaurantId));
        }
    }

    @PostMapping
    public ResponseEntity<?> createReservation(@RequestHeader("Authorization") String token, @PathVariable Long restaurantId, @RequestBody ReservationDTO reservationDTO) {
        try {
            String email = jwtUtils.getUserNameFromJwtToken(token.replace("Bearer ", ""));
            Customer customer = customerService.findCustomerByEmail(email);

            RestaurantTable restaurantTable = restaurantTableService.getRestaurantTableByRestaurantIdAndId(restaurantId, reservationDTO.getRestaurantTable_id());

            Reservation reservation = new Reservation();
            reservation.setStartDateTime(reservationDTO.getStartDateTime());
            reservation.setEndDateTime(reservationDTO.getEndDateTime());
            reservation.setCustomer(customer);
            reservation.setRestaurantTable(restaurantTable);

            return new ResponseEntity<>(reservationService.createReservation(reservation), HttpStatus.CREATED);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
