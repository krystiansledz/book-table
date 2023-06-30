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
import org.springframework.data.domain.Sort;
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

    public List<Restaurant> getAllRestaurants(String name, String sortBy, String direction) {
        Sort sort = null;

        if (sortBy != null) {
            sort = Sort.by(sortBy);
            if (direction != null && direction.equals("desc")) {
                sort = sort.descending();
            } else if (direction != null && direction.equals("asc")) {
                sort = sort.ascending();
            }
        }

        if (name == null || name.isEmpty()) {
            if (sort == null) {
                return restaurantRepository.findAll();
            }
            return restaurantRepository.findAll(sort);
        } else {
            if (sort == null) {
                return restaurantRepository.findByNameContainingIgnoreCase(name);
            }
            return restaurantRepository.findByNameContainingIgnoreCase(name, sort);
        }
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

        // Find all reservations that overlap with the requested reservation time
        List<Reservation> overlappingReservations = reservationRepository.findAllByStartDateTimeLessThanEqualAndEndDateTimeGreaterThanEqual(end, start);

        // Get a list of all tables that are already reserved
        List<RestaurantTable> reservedTables = overlappingReservations.stream()
                .map(Reservation::getRestaurantTable)
                .collect(Collectors.toList());

        // Filter out tables that are already reserved
        return allTables.stream()
                .filter(table -> !reservedTables.contains(table))
                .collect(Collectors.toList());
    }

    public Double calculateAverageRating(Restaurant restaurant) {
        List<RestaurantTable> tables = restaurant.getRestaurantTables();
        double sum = 0.0;
        int count = 0;

        for (RestaurantTable table : tables) {
            List<Reservation> reservations = table.getReservations();
            for (Reservation reservation : reservations) {
                Integer rating = reservation.getRating();
                if (rating != null) {
                    sum += rating;
                    count++;
                }
            }
        }

        return count > 0 ? sum / count : null;
    }
}
