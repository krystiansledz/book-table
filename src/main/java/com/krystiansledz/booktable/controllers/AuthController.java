package com.krystiansledz.booktable.controllers;

import com.krystiansledz.booktable.Utils;
import com.krystiansledz.booktable.models.Customer;
import com.krystiansledz.booktable.models.Restaurant;
import com.krystiansledz.booktable.payload.request.LoginRequest;
import com.krystiansledz.booktable.payload.request.CustomerSignupRequest;
import com.krystiansledz.booktable.payload.request.RestaurantSignupRequest;
import com.krystiansledz.booktable.payload.response.JwtResponse;
import com.krystiansledz.booktable.payload.response.MessageResponse;
import com.krystiansledz.booktable.repository.CustomerRepository;
import com.krystiansledz.booktable.repository.RestaurantRepository;
import com.krystiansledz.booktable.security.jwt.JwtUtils;
import com.krystiansledz.booktable.security.principals.CustomerPrincipal;
import com.krystiansledz.booktable.security.principals.RestaurantPrincipal;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    RestaurantRepository restaurantRepository;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    JwtUtils jwtUtils;

    @PostMapping("/signin/customer")
    public ResponseEntity<?> authenticateCustomer(@Valid @RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        CustomerPrincipal customerPrincipal = (CustomerPrincipal) authentication.getPrincipal();

        return ResponseEntity
                .ok(new JwtResponse(jwt, customerPrincipal.getId(), customerPrincipal.getUsername()));

    }

    @PostMapping("/signin/restaurant")
    public ResponseEntity<?> authenticateRestaurant(@Valid @RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        RestaurantPrincipal restaurantPrincipal = (RestaurantPrincipal) authentication.getPrincipal();

        return ResponseEntity.ok(new JwtResponse(jwt, restaurantPrincipal.getId(), restaurantPrincipal.getUsername()));
    }

    @PostMapping("/signup/customer")
    public ResponseEntity<?> registerCustomer(@Valid @RequestBody CustomerSignupRequest signUpRequest) {
        if (customerRepository.existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Utils.createErrorMap(new String[]{"email"}, new String[]{"Email is already taken"}));
        }

        // Create new customer's account
        Customer customer = new Customer(signUpRequest.getEmail(),
                encoder.encode(signUpRequest.getPassword()));

        customerRepository.save(customer);

        return ResponseEntity.ok(new MessageResponse("Customer registered successfully!"));
    }

    @PostMapping("/signup/restaurant")
    public ResponseEntity<?> registerRestaurant(@Valid @RequestBody RestaurantSignupRequest signUpRequest) {
        if (restaurantRepository.existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Utils.createErrorMap(new String[]{"email"}, new String[]{"Email is already taken"}));
        }

        // Create new restaurant's account
        Restaurant customer = new Restaurant(signUpRequest.getEmail(),
                encoder.encode(signUpRequest.getPassword()), signUpRequest.getName());

        restaurantRepository.save(customer);

        return ResponseEntity.ok(new MessageResponse("Restaurant registered successfully!"));
    }
}
