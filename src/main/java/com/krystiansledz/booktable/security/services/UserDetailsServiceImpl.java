package com.krystiansledz.booktable.security.services;

import com.krystiansledz.booktable.models.Customer;
import com.krystiansledz.booktable.models.Restaurant;
import com.krystiansledz.booktable.repository.CustomerRepository;
import com.krystiansledz.booktable.repository.RestaurantRepository;
import com.krystiansledz.booktable.security.principals.CustomerPrincipal;
import com.krystiansledz.booktable.security.principals.RestaurantPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    RestaurantRepository restaurantRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<Customer> customerOptional = customerRepository.findByEmail(email);
        Optional<Restaurant> restaurantOptional = restaurantRepository.findByEmail(email);

        if (customerOptional.isPresent()) {
            Customer customer = customerOptional.get();
            return CustomerPrincipal.build(customer);
        }

        if (restaurantOptional.isPresent()) {
            Restaurant restaurant = restaurantOptional.get();
            return RestaurantPrincipal.build(restaurant);
        }

        throw new UsernameNotFoundException("User not found with email: " + email);
    }
}
