package com.krystiansledz.booktable.controllers;

import com.krystiansledz.booktable.Utils;
import com.krystiansledz.booktable.models.Customer;
import com.krystiansledz.booktable.models.Restaurant;
import com.krystiansledz.booktable.models.UserType;
import com.krystiansledz.booktable.payload.request.SigninRequest;
import com.krystiansledz.booktable.payload.request.SignupRequest;
import com.krystiansledz.booktable.payload.response.JwtResponse;
import com.krystiansledz.booktable.payload.response.MessageResponse;
import com.krystiansledz.booktable.repository.CustomerRepository;
import com.krystiansledz.booktable.repository.RestaurantRepository;
import com.krystiansledz.booktable.security.jwt.JwtUtils;
import com.krystiansledz.booktable.security.services.UserDetailsServiceImpl;
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
    UserDetailsServiceImpl userDetailsService;


    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    RestaurantRepository restaurantRepository;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    JwtUtils jwtUtils;

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody SigninRequest signinRequest) {
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(signinRequest.getEmail(), signinRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        return ResponseEntity.ok(new JwtResponse(jwt));
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
        if (customerRepository.existsByEmail(signUpRequest.getEmail()) || restaurantRepository.existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Utils.createErrorMap(new String[]{"email"}, new String[]{"Email is already taken"}));
        }

        if (signUpRequest.getUserType().equalsIgnoreCase(UserType.CUSTOMER.toString())) {
            // Create new customer's account
            Customer customer = new Customer(signUpRequest.getEmail(),
                    encoder.encode(signUpRequest.getPassword()));
            customerRepository.save(customer);
            return ResponseEntity.ok(new MessageResponse("Customer registered successfully!"));
        }


        if (signUpRequest.getUserType().equalsIgnoreCase(UserType.RESTAURANT.toString())) {
            // Create new restaurant's account
            Restaurant restaurant = new Restaurant(signUpRequest.getEmail(),
                    encoder.encode(signUpRequest.getPassword()), signUpRequest.getName());
            restaurantRepository.save(restaurant);
            return ResponseEntity.ok(new MessageResponse("Restaurant registered successfully!"));
        }

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Something went wrong");
    }
}
