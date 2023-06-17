package com.krystiansledz.booktable.repository;

import com.krystiansledz.booktable.models.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    List<Reservation> findByTableIdAndStartIsBetweenOrEndIsBetween(Long restaurantTableId, LocalDateTime start_from, LocalDateTime start_to, LocalDateTime end_from, LocalDateTime end_to);
}