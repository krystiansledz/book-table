package com.krystiansledz.booktable.repository;

import com.krystiansledz.booktable.models.RestaurantTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RestaurantTableRepository extends JpaRepository<RestaurantTable, Long> {
    List<RestaurantTable> findAllByRestaurantId(Long restaurantId);

    Optional<RestaurantTable> findByNumberAndRestaurantId(Integer number, Long restaurantId);
}