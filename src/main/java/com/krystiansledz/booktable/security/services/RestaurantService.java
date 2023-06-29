package com.krystiansledz.booktable.security.services;

import com.krystiansledz.booktable.models.BusinessHours;
import com.krystiansledz.booktable.models.Reservation;
import com.krystiansledz.booktable.models.Restaurant;
import com.krystiansledz.booktable.models.RestaurantTable;
import com.krystiansledz.booktable.repository.BusinessHoursRepository;
import com.krystiansledz.booktable.repository.ReservationRepository;
import com.krystiansledz.booktable.repository.RestaurantRepository;
import com.krystiansledz.booktable.repository.RestaurantTableRepository;
import com.krystiansledz.booktable.security.principals.RestaurantPrincipal;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class RestaurantService implements UserDetailsService {
    @Autowired
    RestaurantRepository restaurantRepository;
    @Autowired
    RestaurantTableRepository restaurantTableRepository;
    @Autowired
    ReservationRepository reservationRepository;
    @Autowired
    BusinessHoursRepository businessHoursRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Restaurant restaurant = restaurantRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found with email: " + email));

        return RestaurantPrincipal.build(restaurant);
    }

    public List<Restaurant> getAllRestaurants() {
        return restaurantRepository.findAll();
    }

    public Restaurant getRestaurantById(Long id) {
        return restaurantRepository.findById(id).orElseThrow(EntityNotFoundException::new);
    }

    public Restaurant createRestaurant(Restaurant restaurant) {
        return restaurantRepository.save(restaurant);
    }

    public Restaurant updateRestaurant(Restaurant restaurant) {
        return restaurantRepository.save(restaurant);
    }

    public Restaurant partialUpdateRestaurant(Long id, Map<String, Object> updates) {
        Restaurant restaurant = getRestaurantById(id);

        for (Map.Entry<String, Object> entry : updates.entrySet()) {
            String key = entry.getKey();
            switch (key) {
                case "name":
                    restaurant.setName((String) entry.getValue());
                    break;
                case "address":
                    restaurant.setAddress((String) entry.getValue());
                    break;
            }
        }

        return restaurantRepository.save(restaurant);
    }

    public List<RestaurantTable> getRestaurantAvailableTables(Long restaurantId, LocalDateTime start, LocalDateTime end) {
        // Find the opening hours for the day of the week for start time
        DayOfWeek reservationDayOfWeek = start.getDayOfWeek();
        BusinessHours businessHours = businessHoursRepository.findByDayOfWeekAndRestaurantId(reservationDayOfWeek, restaurantId)
                .orElseThrow(() -> new IllegalArgumentException("Restaurant is not open on " + reservationDayOfWeek));

        // Check if reservation start and end times are within opening hours
        LocalTime openingTime = businessHours.getOpeningTime();
        LocalTime closingTime = businessHours.getClosingTime();
        if (start.toLocalTime().isBefore(openingTime) || end.toLocalTime().isAfter(closingTime)) {
            throw new IllegalArgumentException("Reservation time is outside of restaurant's opening hours");
        }

        // Find all tables for the restaurant
        List<RestaurantTable> allTables = restaurantTableRepository.findAllByRestaurantId(restaurantId);

        // Filter out tables which are reserved during the requested reservation time

        return allTables.stream()
                .filter(table -> {
                    List<Reservation> tableReservations = reservationRepository.findByRestaurantTableIdAndStartDateTimeIsBetweenOrEndDateTimeIsBetween(
                            table.getId(), start, end, start, end);

                    return tableReservations.isEmpty();
                })
                .collect(Collectors.toList());
    }
}
