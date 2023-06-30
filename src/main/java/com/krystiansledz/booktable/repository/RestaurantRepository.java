package com.krystiansledz.booktable.repository;

import com.krystiansledz.booktable.models.Restaurant;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {
    Optional<Restaurant> findByEmail(String email);

    Boolean existsByEmail(String email);

    List<Restaurant> findByNameContainingIgnoreCase(String name);

    List<Restaurant> findByNameContainingIgnoreCase(String name, Sort sort);

}