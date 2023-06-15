package com.krystiansledz.booktable.security.principals;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.krystiansledz.booktable.models.Restaurant;
import com.krystiansledz.booktable.models.UserType;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.Collections;
import java.util.Objects;

public class RestaurantPrincipal implements UserPrincipal {
    private Long id;
    private String email;
    @JsonIgnore
    private String password;
    private String name;
    private UserType userType;

    public RestaurantPrincipal(Long id, String email, String password, String name, UserType userType) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.name = name;
        this.userType = userType;
    }

    public static RestaurantPrincipal build(Restaurant restaurant) {
        return new RestaurantPrincipal(restaurant.getId(),
                restaurant.getEmail(),
                restaurant.getPassword(),
                restaurant.getName(),
                restaurant.getUserType()
        );
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

    public String getName() {
        return name;
    }

    public UserType getUserType() {
        return userType;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority("RESTAURANT"));
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
        RestaurantPrincipal restaurant = (RestaurantPrincipal) o;
        return Objects.equals(id, restaurant.id);
    }

}
