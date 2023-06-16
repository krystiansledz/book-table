package com.krystiansledz.booktable.security.services;

import com.krystiansledz.booktable.models.Restaurant;
import com.krystiansledz.booktable.repository.RestaurantRepository;
import com.krystiansledz.booktable.security.principals.RestaurantPrincipal;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
public class RestaurantService implements UserDetailsService {
    @Autowired
    RestaurantRepository restaurantRepository;

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
}
