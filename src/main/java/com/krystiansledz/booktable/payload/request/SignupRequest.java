package com.krystiansledz.booktable.payload.request;

import com.krystiansledz.booktable.validation.ConditionalValidation;
import jakarta.validation.constraints.NotBlank;

@ConditionalValidation(
        conditionalProperty = "userType", values = {"restaurant"},
        requiredProperties = {"name"},
        message = "must not be blank")
public class SignupRequest {
    @NotBlank
    private String email;

    @NotBlank
    private String password;
    @NotBlank
    private String userType;

    private String name;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}