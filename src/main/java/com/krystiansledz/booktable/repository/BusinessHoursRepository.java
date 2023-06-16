package com.krystiansledz.booktable.repository;

import com.krystiansledz.booktable.models.BusinessHours;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.DayOfWeek;
import java.util.Optional;

@Repository
public interface BusinessHoursRepository extends JpaRepository<BusinessHours, Long> {
    Optional<BusinessHours> findByDayOfWeekAndRestaurantId(DayOfWeek dayOfWeek, Long restaurantId);
}
