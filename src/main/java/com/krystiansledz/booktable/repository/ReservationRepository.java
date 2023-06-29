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
    List<Reservation> findByRestaurantTableIdAndStartDateTimeIsBetweenOrEndDateTimeIsBetween(Long restaurantTableId, LocalDateTime start_from, LocalDateTime start_to, LocalDateTime end_from, LocalDateTime end_to);

    List<Reservation> findAllByRestaurantTable_Restaurant_Id(Long restaurantId);

    List<Reservation> findAllByRestaurantTable_Restaurant_IdAndStartDateTimeBetween(Long restaurantId, LocalDateTime startDate, LocalDateTime endDate);

    @Query("SELECT r FROM Reservation r WHERE r.restaurantTable = :restaurantTable AND ((r.startDateTime < :endDateTime AND r.endDateTime > :startDateTime))")
    List<Reservation> findAllByRestaurantTableAndTimeRange(@Param("restaurantTable") RestaurantTable restaurantTable, @Param("startDateTime") LocalDateTime startDateTime, @Param("endDateTime") LocalDateTime endDateTime);

    List<Reservation> findByCustomer(Customer customer);
}