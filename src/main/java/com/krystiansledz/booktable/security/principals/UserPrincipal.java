package com.krystiansledz.booktable.security.principals;

import com.krystiansledz.booktable.models.UserType;
import org.springframework.security.core.userdetails.UserDetails;

public interface UserPrincipal extends UserDetails {
    Long getId();

    String getEmail();

    String getPassword();

    UserType getUserType();
}