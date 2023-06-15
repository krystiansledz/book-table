package com.krystiansledz.booktable.security.services;

import com.krystiansledz.booktable.models.Restaurant;
import com.krystiansledz.booktable.repository.RestaurantRepository;
import com.krystiansledz.booktable.security.principals.RestaurantPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
}
