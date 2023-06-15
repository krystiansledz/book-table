package com.krystiansledz.booktable.repository;

import com.krystiansledz.booktable.models.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {
    Optional<Restaurant> findByEmail(String email);

    Boolean existsByEmail(String email);
    
}