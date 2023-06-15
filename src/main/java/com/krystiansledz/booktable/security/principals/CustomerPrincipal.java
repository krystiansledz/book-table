package com.krystiansledz.booktable.security.principals;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.krystiansledz.booktable.models.Customer;
import com.krystiansledz.booktable.models.UserType;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.Collections;
import java.util.Objects;

public class CustomerPrincipal implements UserPrincipal {
    private Long id;
    private String email;
    @JsonIgnore
    private String password;
    private UserType userType;

    public CustomerPrincipal(Long id, String email, String password, UserType userType) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.userType = userType;
    }

    public static CustomerPrincipal build(Customer customer) {
        return new CustomerPrincipal(customer.getId(),
                customer.getEmail(),
                customer.getPassword(),
                customer.getUserType()
        );
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority("CUSTOMER"));
    }

    @Override
    public String getUsername() {
        return email;
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


    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        CustomerPrincipal customer = (CustomerPrincipal) o;
        return Objects.equals(id, customer.id);
    }

}
