package com.krystiansledz.booktable.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

@Entity
@Table(name = "reservations")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "start_time")
    private LocalDateTime start;

    @NotNull
    @Column(name = "end_time")
    private LocalDateTime end;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "customer_id")
    @JsonBackReference
    private Customer customer;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "restaurant_table_id")
    @JsonBackReference
    private RestaurantTable table;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getStart() {
        return start;
    }

    public void setStart(LocalDateTime start) {
        this.start = start;
    }

    public LocalDateTime getEnd() {
        return end;
    }

    public void setEnd(LocalDateTime end) {
        this.end = end;
    }

    @JsonProperty("customer_id")
    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(@JsonProperty("customer_id") Customer customer) {
        this.customer = customer;
    }

    @JsonProperty("restaurant_table_id")
    public RestaurantTable getRestaurantTable() {
        return table;
    }

    public void setRestaurantTable(@JsonProperty("restaurant_table_id") RestaurantTable table) {
        this.table = table;
    }
}
