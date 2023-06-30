package com.krystiansledz.booktable.security.services;

import com.krystiansledz.booktable.models.Customer;
import com.krystiansledz.booktable.models.Reservation;
import com.krystiansledz.booktable.models.RestaurantTable;
import com.krystiansledz.booktable.repository.ReservationRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class ReservationService {

    @Autowired
    private ReservationRepository reservationRepository;

    public List<Reservation> getAllReservations() {
        return reservationRepository.findAll();
    }

    public List<Reservation> getAllReservationsByRestaurant(Long id) {
        return reservationRepository.findAllByRestaurantTable_Restaurant_Id(id);
    }

    public List<Reservation> getReservationsByDate(String date, Long restaurantId) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDateTime startDate = LocalDate.parse(date, formatter).atStartOfDay();
        LocalDateTime endDate = startDate.plusDays(1);
        return reservationRepository.findAllByRestaurantTable_Restaurant_IdAndStartDateTimeBetween(restaurantId, startDate, endDate);
    }

    public Reservation getReservationById(Long id) {
        return reservationRepository.findById(id).orElseThrow(EntityNotFoundException::new);
    }

    public List<Reservation> getReservationsByCustomer(Customer customer) {
        return reservationRepository.findByCustomer(customer);
    }

    public Reservation createReservation(Reservation reservation) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime start = reservation.getStartDateTime();
        LocalDateTime end = reservation.getEndDateTime();
        RestaurantTable table = reservation.getRestaurantTable();

        if (start.isBefore(now)) {
            throw new IllegalArgumentException("Start time of the reservation cannot be in the past.");
        }

        if (start.isAfter(end)) {
            throw new IllegalArgumentException("Start time of the reservation cannot be after the end time.");
        }

        List<Reservation> overlappingReservations = reservationRepository.findAllByRestaurantTableAndTimeRange(table, start, end);

        if (overlappingReservations.isEmpty()) {
            return reservationRepository.save(reservation);
        } else {
            throw new IllegalStateException("Table is not available during the requested time.");
        }
    }


    public void updateReservation(Reservation reservation) {
        reservationRepository.save(reservation);
    }

    public void deleteReservation(Long id) {
        reservationRepository.deleteById(id);
    }
}

