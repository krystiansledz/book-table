package com.krystiansledz.booktable.repository;

import com.krystiansledz.booktable.models.RestaurantTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RestaurantTableRepository extends JpaRepository<RestaurantTable, Long> {
}