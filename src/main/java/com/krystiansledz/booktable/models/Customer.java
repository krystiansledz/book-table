package com.krystiansledz.booktable.models;

import com.krystiansledz.booktable.enums.UserType;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "customers",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "email")
        })
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 50)
    @Email
    private String email;

    @NotBlank
    @Size(max = 120)
    private String password;

    @Enumerated(EnumType.STRING)
    private UserType userType = UserType.CUSTOMER;

    public Customer() {
    }

    public Customer(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public UserType getUserType() {
        return userType;
    }
}
