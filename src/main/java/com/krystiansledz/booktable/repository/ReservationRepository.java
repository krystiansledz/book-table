package com.krystiansledz.booktable.repository;

import com.krystiansledz.booktable.models.Customer;
import com.krystiansledz.booktable.models.Reservation;
import com.krystiansledz.booktable.models.RestaurantTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    @Query("SELECT r FROM Reservation r WHERE r.startDateTime <= :endTime AND r.endDateTime >= :startTime")
    List<Reservation> findAllByStartDateTimeLessThanEqualAndEndDateTimeGreaterThanEqual(@Param("endTime") LocalDateTime endTime, @Param("startTime") LocalDateTime startTime);

    List<Reservation> findAllByRestaurantTable_Restaurant_Id(Long restaurantId);

    List<Reservation> findAllByRestaurantTable_Restaurant_IdAndStartDateTimeBetween(Long restaurantId, LocalDateTime startDate, LocalDateTime endDate);

    @Query("SELECT r FROM Reservation r WHERE r.restaurantTable = :restaurantTable AND ((r.startDateTime < :endDateTime AND r.endDateTime > :startDateTime))")
    List<Reservation> findAllByRestaurantTableAndTimeRange(@Param("restaurantTable") RestaurantTable restaurantTable, @Param("startDateTime") LocalDateTime startDateTime, @Param("endDateTime") LocalDateTime endDateTime);

    List<Reservation> findByCustomer(Customer customer);
}