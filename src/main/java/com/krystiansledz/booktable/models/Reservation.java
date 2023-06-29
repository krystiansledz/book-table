package com.krystiansledz.booktable.models;

import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

@Entity
@Table(name = "reservations")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private LocalDateTime startDateTime;

    @NotNull
    private LocalDateTime endDateTime;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "customer_id")
    @JsonBackReference(value = "customer-reservations")
    private Customer customer;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "restaurant_table_id")
    @JsonBackReference(value = "restaurantTable-reservations")
    private RestaurantTable restaurantTable;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getStartDateTime() {
        return startDateTime;
    }

    public void setStartDateTime(LocalDateTime start) {
        this.startDateTime = start;
    }

    public LocalDateTime getEndDateTime() {
        return endDateTime;
    }

    public void setEndDateTime(LocalDateTime end) {
        this.endDateTime = end;
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
        return restaurantTable;
    }

    public void setRestaurantTable(@JsonProperty("restaurant_table_id") RestaurantTable table) {
        this.restaurantTable = table;
    }

    @Override
    public String toString() {
        return "Reservation{" +
                "id=" + id +
                ", startDateTime=" + startDateTime +
                ", endDateTime=" + endDateTime +
                ", customer=" + customer +
                ", restaurantTable=" + restaurantTable +
                '}';
    }
}
