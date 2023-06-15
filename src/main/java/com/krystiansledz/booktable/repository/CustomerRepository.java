package com.krystiansledz.booktable.repository;

import com.krystiansledz.booktable.models.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    Optional<Customer> findByEmail(String email);
    
    Boolean existsByEmail(String email);

}