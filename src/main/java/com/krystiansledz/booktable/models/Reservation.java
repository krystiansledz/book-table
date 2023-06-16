package com.krystiansledz.booktable.models;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "reservations")
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private Integer numberOfPeople;

    @ManyToOne
    private Customer customer;

    @ManyToOne
    private RestaurantTable table;
}
